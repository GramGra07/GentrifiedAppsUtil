package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles.controllers

import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * SquIDController is a controller that uses the square root of the error to calculate the output.
 * @param p The proportional gain.
 * @param d The derivative gain. **UNUSED**
 */
class SquIDController(var p: Double, var d: Double) {
    constructor(p: Double) : this(p, 0.0)

    fun calculate(setpoint: Double, current: Double): Double {
        return (sqrt(abs((setpoint - current) * p)) * sign(setpoint - current))
//        +(d * setpoint - current)
    }
}