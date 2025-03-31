package org.gentrifiedApps.gentrifiedAppsUtil.motionProfiles

import com.qualcomm.robotcore.util.ElapsedTime

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