package com.dacodingbeast.pidtuners.PID

import kotlin.math.abs
import kotlin.math.sin

class SlidePID(pidfCoefs: PIDFCoefs) {
    var pidfCoefs: PIDFCoefs = pidfCoefs
    var lastTime: Double = 0.0
    var prevMeasure: Double = 0.0
    var totalError: Double = 0.0
    var errorValP: Double = 0.0
    var errorValV: Double = 0.0
    var settled: Boolean = false
    var setPoint: Double = 0.0

    fun calculateCoupled(measured: Double, setPoint: Double, theta: Double, now: Double = System.nanoTime() / 1e9): Double {
        this.setPoint = setPoint

        val currentTimeStamp = now
        if (lastTime == 0.0) {
            lastTime = currentTimeStamp
            prevMeasure = measured
        }

        var period = currentTimeStamp - lastTime
        lastTime = currentTimeStamp

        if (period < 1e-6) {
            period = 1e-6
        }

        errorValP = this.setPoint - measured

        if (abs(errorValP) < 1e-6 && !settled) {
            settled = true
        }

        errorValV = -(measured - prevMeasure) / period
        prevMeasure = measured
        totalError += period * errorValP

        val ffWord = pidfCoefs.f * sin(theta)

        return pidfCoefs.p * errorValP +
            pidfCoefs.i * totalError +
            pidfCoefs.d * errorValV +
            ffWord
    }
    fun calculate(measured: Double, setPoint: Double, now: Double = System.nanoTime() / 1e9): Double {
        this.setPoint = setPoint

        val currentTimeStamp = now
        if (lastTime == 0.0) {
            lastTime = currentTimeStamp
            prevMeasure = measured
        }

        var period = currentTimeStamp - lastTime
        lastTime = currentTimeStamp

        if (period < 1e-6) {
            period = 1e-6
        }

        errorValP = this.setPoint - measured

        if (abs(errorValP) < 1e-6 && !settled) {
            settled = true
        }

        errorValV = -(measured - prevMeasure) / period
        prevMeasure = measured
        totalError += period * errorValP

        val ffWord = pidfCoefs.f * 0

        return pidfCoefs.p * errorValP +
                pidfCoefs.i * totalError +
                pidfCoefs.d * errorValV +
                ffWord
    }
}