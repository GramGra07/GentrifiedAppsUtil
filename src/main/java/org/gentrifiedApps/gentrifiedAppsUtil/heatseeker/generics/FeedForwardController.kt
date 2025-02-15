package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import kotlin.math.sign

class FeedforwardController(private val kS: Double, private val kV: Double, private val kA: Double) {
    fun calculate(targetVelocity: Double, targetAcceleration: Double): Double {
        return (kS * sign(targetVelocity)) + (kV * targetVelocity) + (kA * targetAcceleration)
    }
}
