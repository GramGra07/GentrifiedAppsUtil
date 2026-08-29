package org.gentrifiedApps.gentrifiedAppsUtil.motion.controllers

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pid.PIDCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pid.PIDFCoefficients
import kotlin.math.abs

class PIDController(kP: Double, kI: Double, kD: Double) : PIDFController(kP, kI, kD, 0.0) {
    constructor(pidCoefficients: PIDCoefficients) : this(
        pidCoefficients.kP,
        pidCoefficients.kI,
        pidCoefficients.kD
    )

    fun setPID(kP: Double, kI: Double, kD: Double) {
        setPIDF(kP, kI, kD, 0.0)
    }
}

open class PIDFController(var kP: Double, var kI: Double, var kD: Double, var kF: Double) {
    private var setPoint: Double = 0.0
    private var measuredValue: Double = 0.0

    private var minIntegral: Double = Double.NEGATIVE_INFINITY
    private var maxIntegral: Double = Double.POSITIVE_INFINITY

    private var errorValP: Double = 0.0
    private var errorValV: Double = 0.0
    private var totalError: Double = 0.0
    private var prevMeasuredValue: Double = 0.0

    private var errorToleranceP: Double = 0.05
    private var errorToleranceV: Double = Double.POSITIVE_INFINITY

    private var lastTimeStamp: Double = 0.0
    private var period: Double = 0.02

    constructor(kP: Double, kI: Double, kD: Double, kF: Double, sp: Double, pv: Double) : this(
        kP,
        kI,
        kD,
        kF
    ) {
        setPoint = sp
        measuredValue = pv
        errorValP = setPoint - measuredValue
        reset()
    }

    // Assuming PIDFCoefficients is a data class defined elsewhere
    constructor(pidfCoefficients: PIDFCoefficients) : this(
        pidfCoefficients.kP,
        pidfCoefficients.kI,
        pidfCoefficients.kD,
        pidfCoefficients.kF
    ) {
        reset()
    }

    constructor() : this(0.0, 0.0, 0.0, 0.0) {
        reset()
    }

    fun reset() {
        totalError = 0.0
        lastTimeStamp = 0.0
    }

    fun setTolerance(
        positionTolerance: Double,
        velocityTolerance: Double = Double.POSITIVE_INFINITY
    ) {
        errorToleranceP = positionTolerance
        errorToleranceV = velocityTolerance
    }

    fun getSetPoint(): Double = setPoint

    fun setSetPoint(sp: Double) {
        setPoint = sp
    }

    fun atSetPoint(): Boolean {
        return abs(errorValP) < errorToleranceP && abs(errorValV) < errorToleranceV
    }

    fun getCoefficients(): DoubleArray = doubleArrayOf(kP, kI, kD, kF)
    fun getPositionError(): Double = errorValP
    fun getTolerance(): DoubleArray = doubleArrayOf(errorToleranceP, errorToleranceV)
    fun getVelocityError(): Double = errorValV

    fun calculate(): Double = calculate(measuredValue)

    fun calculate(pv: Double, sp: Double): Double {
        setSetPoint(sp)
        return calculate(pv)
    }

    fun calculate(pv: Double): Double {
        val currentTimeStamp = System.nanoTime().toDouble() / 1e9

        if (lastTimeStamp == 0.0) {
            lastTimeStamp = currentTimeStamp
            prevMeasuredValue = pv
        }

        period = currentTimeStamp - lastTimeStamp
        lastTimeStamp = currentTimeStamp

        if (period <= 1e-6) {
            period = 1e-6
        }

        errorValP = setPoint - pv
        measuredValue = pv

        errorValV = -(measuredValue - prevMeasuredValue) / period
        prevMeasuredValue = measuredValue

        totalError += period * errorValP
        totalError = totalError.coerceIn(minIntegral, maxIntegral)

        return (kP * errorValP) + (kI * totalError) + (kD * errorValV) + (kF * setPoint)
    }

    fun setPIDF(kP: Double, kI: Double, kD: Double, kF: Double) {
        this.kP = kP
        this.kI = kI
        this.kD = kD
        this.kF = kF
    }

    fun setPIDF(pidfCoefficients: PIDFCoefficients) {
        this.kP = pidfCoefficients.kP
        this.kI = pidfCoefficients.kI
        this.kD = pidfCoefficients.kD
        this.kF = pidfCoefficients.kF
    }

    fun setIntegrationBounds(integralMin: Double, integralMax: Double) {
        minIntegral = integralMin
        maxIntegral = integralMax
    }

    fun clearTotalError() {
        totalError = 0.0
    }
}