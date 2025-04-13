package org.gentrifiedApps.gentrifiedAppsUtil.idler

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

/**
 * A class to help with safe sleep actions, which FTC typically does not have.
 */
class Idler {
    companion object {
        private var startTime = 0.0

        init {
            resetTimer()
        }

        private fun resetTimer() {
            startTime = System.currentTimeMillis().toDouble()
        }

        private fun getElapsedSeconds(): Double {
            return (System.currentTimeMillis().toDouble() - startTime) / 1000
        }

        @JvmStatic
        fun safeIdle(time: Double, opMode: LinearOpMode) {
            safeIdle(time, opMode, Runnable {})
        }

        /**
         * Idles for a given amount of time.
         * @param time The amount of time to idle for.
         * @param updateWhileIdling A lambda to run while idling.
         */
        @JvmStatic
        fun safeIdle(time: Double, opMode: LinearOpMode, updateWhileIdling: Runnable) {
            require(time > 0) { "Time must be greater than 0" }
            require(time < 30.0) { "Time must be less than 30" }
            resetTimer()
            while (getElapsedSeconds() < time && opMode.opModeIsActive() == true && !opMode.isStopRequested) {
                updateWhileIdling.run()
                Scribe.instance.setSet("I")
                    .logDebug(
                        "Idling for $time seconds, ${
                            String.format(
                                "%.0f",
                                (getElapsedSeconds() / time) * 100
                            )
                        }%"
                    )
            }
        }
    }
}