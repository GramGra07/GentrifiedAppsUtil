package org.gentrifiedApps.gentrifiedAppsUtil.idler

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

/**
 * A class to help with safe sleep actions, which FTC typically does not have.
 */
class Idler(private val opMode: LinearOpMode) {
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
        require(time>0) { "Time must be greater than 0" }
        require(time<30.0){ "Time must be less than 30" }
        elapsed.reset()
        while (elapsed.seconds() < time && opMode.opModeIsActive() == true && !opMode.isStopRequested) {
            updateWhileIdling.run()
            Scribe.instance.setSet("I").logDebug("Idling for $time seconds, $elapsed seconds elapsed")
        }
    }
}