package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

class PIDController(private val kP: Double, private val kI: Double, private val kD: Double) {
    private var lastError = 0.0
    private var integralSum = 0.0

    fun calculate(error: Double): Double {
        val derivative = error - lastError
        integralSum += error

        lastError = error
        return (kP * error) + (kI * integralSum) + (kD * derivative)
    }
}
