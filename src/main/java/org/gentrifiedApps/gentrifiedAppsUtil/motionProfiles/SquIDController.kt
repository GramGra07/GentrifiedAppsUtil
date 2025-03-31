package org.gentrifiedApps.gentrifiedAppsUtil.motionProfiles

class SquIDController(var p:Double,var d:Double){
    constructor(p:Double):this(p,0.0) 

    fun calculate(setpoint: Double, current: Double): Double {
        return (kotlin.math.sqrt(kotlin.math.abs((setpoint - current) * p)) * kotlin.math.sign(setpoint - current)) +
                (d * setpoint - current)
    }
}