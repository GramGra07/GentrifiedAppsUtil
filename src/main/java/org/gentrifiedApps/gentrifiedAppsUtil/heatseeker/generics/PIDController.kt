package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

class PIDController(private val kp: Double, private val ki: Double, private val kd: Double) {
    private var previousError = 0.0
    private var integral = 0.0

    fun calculatePID(setpoint: Double, current: Double): Double {
        val error = setpoint - current
        integral += error
        val derivative = error - previousError
        previousError = error
        return kp * error + ki * integral + kd * derivative
    }
}