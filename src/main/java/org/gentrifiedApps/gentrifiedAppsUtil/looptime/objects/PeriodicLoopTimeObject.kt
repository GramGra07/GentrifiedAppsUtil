package org.gentrifiedApps.gentrifiedAppsUtil.looptime.objects

import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * A class to run a function every given amount of loops.
 * @param name The name of the object
 * @param everyLoopNum The amount of loops to wait to run the function
 * @param func The function to run
 */
class PeriodicLoopTimeObject(val name: String, everyLoopNum: Int, private val func: Runnable) {
    private val interval: Int = everyLoopNum
    private var refreshRate: Double = 0.0
    private var pastRefreshRate: Double = refreshRate
    private var realRefreshRate: Double = 0.0
    private var pastTimeRR: Double = 0.0

    /**
     * Updates the refresh rate
     * @param currentTime The current time
     */
    fun rr(currentTime: Double) {
        if (refreshRate != pastRefreshRate) {
            realRefreshRate = currentTime - pastTimeRR
            pastRefreshRate = refreshRate
            pastTimeRR = currentTime
        }
    }

    fun run() {
        func.run()
    }

    fun check(loops: Int): Boolean {
        return loops % interval == 0
    }

    fun telemetry(telemetry: Telemetry) {
        telemetry.addData("$name Refresh Rate", "%.1f", realRefreshRate)
    }
}