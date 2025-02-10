package org.gentrifiedApps.gentrifiedAppsUtil.idler

import com.qualcomm.robotcore.util.ElapsedTime

/**
 * A class to help with safe sleep actions, which FTC typically does not have.
 */
class Idler {
    private val elapsed = ElapsedTime()

    init {
        elapsed.reset()
    }

    /**
     * Idles for a given amount of time.
     * @param time The amount of time to idle for.
     * @param updateWhileIdling A lambda to run while idling.
     */
    fun safeIdle(time: Double, updateWhileIdling: Runnable) {
        elapsed.reset()
        while (elapsed.seconds() < time) {
            updateWhileIdling.run()
        }
    }
}