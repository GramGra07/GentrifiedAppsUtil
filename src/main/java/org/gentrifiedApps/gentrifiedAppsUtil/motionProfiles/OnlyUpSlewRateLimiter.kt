package org.gentrifiedApps.gentrifiedAppsUtil.motionProfiles

import com.qualcomm.robotcore.util.ElapsedTime

class OnlyUpSlewRateLimiter(val rateLimit: Double) {
    private var lastOutput = 0.0
    private val timer = ElapsedTime()

    init {
        timer.reset()
    }

    fun calculate(input: Double): Double {
        val deltaTime = timer.seconds()
        timer.reset()

        val maxDelta = rateLimit * deltaTime
        val delta = input - lastOutput

        // Allow negative deltas and zero, limit only positive deltas
        if (delta > maxDelta) {
            lastOutput += maxDelta
        } else {
            lastOutput = input
        }

        return lastOutput
    }
}