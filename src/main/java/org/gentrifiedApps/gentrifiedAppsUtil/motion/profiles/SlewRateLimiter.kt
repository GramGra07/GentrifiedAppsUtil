package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles

import com.qualcomm.robotcore.util.ElapsedTime

/**
 * Must be used with FLOAT zero power braking
 */
class SlewRateLimiter(val rateLimit: Double) {
    private var lastOutput = 0.0
    private val timer = ElapsedTime()

    init {
        timer.reset()
    }

    fun calculate(input: Double): Double {
        val deltaTime = timer.seconds()
        timer.reset()

        val maxDelta = rateLimit * deltaTime
        val delta = (input - lastOutput).coerceIn(-maxDelta, maxDelta)

        lastOutput += delta
        return lastOutput
    }
}