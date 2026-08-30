package com.dacodingbeast.pidtuners.PID

import com.dacodingbeast.pidtuners.utilities.Coupling
import com.dacodingbeast.pidtuners.utilities.MathFunctions.MathFunctions
import kotlin.math.abs
import kotlin.math.cos

class PivotPID(var pidfCoefs: PivotPIDFCoefs) {
    var lastTime: Double = 0.0
    var prevMeasure: Double = 0.0
    var totalError: Double = 0.0
    var errorValP: Double = 0.0
    var errorValV: Double = 0.0
    var settled: Boolean = false
    var setPoint: Double = 0.0

    fun calculateCoupled(measured:Double, setpoint:Double,now:Double = System.nanoTime()/1e9, ticks: Double, coupling: Coupling): Double{
        val top = ticks - coupling.slide_min_ticks
        val quotient = top/ coupling.gamma_dif()
        pidfCoefs.p = coupling.scal_p()*quotient + coupling.p_pidfCoefs.rangeP.low
        pidfCoefs.i = coupling.scal_i()*quotient + coupling.p_pidfCoefs.rangeI.low
        pidfCoefs.d = coupling.scal_d()*quotient + coupling.p_pidfCoefs.rangeD.low
        pidfCoefs.f = coupling.scal_f()*quotient + coupling.p_pidfCoefs.rangeF.low
        this.setPoint = setpoint

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

        val ffPower = pidfCoefs.f * cos(measured)

        return MathFunctions.clamp(pidfCoefs.p * errorValP +
                pidfCoefs.i * totalError +
                pidfCoefs.d * errorValV +
                ffPower, -1.0,1.0)
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

        val ffPower = pidfCoefs.f * cos(measured)

        return MathFunctions.clamp(pidfCoefs.p * errorValP +
            pidfCoefs.i * totalError +
            pidfCoefs.d * errorValV +
            ffPower,-1.0,1.0)
    }
}