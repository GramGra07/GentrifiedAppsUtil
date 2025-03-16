package org.gentrifiedApps.gentrifiedAppsUtil.looptime

import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.objects.PeriodicLoopTimeObject

/**
 * A class to return loop time information and load periodic loop time objects and .every functions
 * @param periodics The periodic loop time objects to load
 */
open class LoopTimeController(
    private val periodics: List<PeriodicLoopTimeObject?>? = null,
) {
    constructor() : this(null)

    private val timer = ElapsedTime()
    var loops: Int = 0
    private var lps = 0.0
    private var currentTime: Double = 0.0
    private var currentTimems: Double = 0.0
    private val correctedLPS: Double = 5.0
    private var lastSecond: Double = 0.0
    private var lastSecondLPast: Double = 0.0
    private var lastSecondL: Double = 0.0
    private var lastSecondT: Double = 0.0


    private var startTime: Double
    private var lastTime: Double
    var deltaTime: Double = 0.0

    init {
        timer.reset()
        this.startTime = timer.milliseconds()
        this.lastTime = startTime
    }

    private fun doCalculations() {
        lps = loops / (currentTime - correctedLPS)
        if (currentTime > correctedLPS) {
            loops++
        }
        if (currentTime - 1 > lastSecondT) {
            lastSecondL = loops - lastSecondLPast
            lastSecond = lastSecondL / (currentTime - lastSecondT)
            lastSecondLPast = loops.toDouble()
            lastSecondT = currentTime
        }
    }

    /**
     * Updates the loop time controller
     */
    open fun update() {
        currentTimems = timer.milliseconds()
        currentTime = timer.seconds()
        doCalculations()
        periodics?.forEach { obj ->
            obj!!.rr(currentTime)
            if (obj.check(loops)) {
                obj.run()
            }
        }
        deltaTime = currentTimems - lastTime
        lastTime = currentTimems
        if (lastSecond < 30) {
            Scribe.instance.logDebug("Loops dropped past 30, this may cause issues and lag")
        }
    }

    /**
     * Telemetry for the loop time controller
     * @param telemetry The telemetry to use, opmode.telemetry
     */

    fun telemetry(telemetry: Telemetry) {
        telemetry.addData("LOOP TIME", "")
        telemetry.addData("Timer", "%.1f", currentTime)
        telemetry.addData("Loops", loops)
        telemetry.addData("Current LPS", "%.1f", lps)
        telemetry.addData("Time Elapsed", "%.1f", deltaTime)
        telemetry.addData("Avg Time Elapsed", "%.1f", (currentTimems - startTime) / loops)
//        telemetry.addData(comparisonDate, "%.1f", comparison-lps)
//        telemetry.addData("lastsecondloops",lastSecondL)
//        telemetry.addData("pastlastsecondloops",lastSecondLPast)
//        telemetry.addData("lastsecondt",lastSecondT)
        telemetry.addData("Last Second LPS", "%.1f", lastSecond)
    }

    companion object {
        /**
         * A function to run a function every given amount of loops.
         * @param period The amount of loops to wait to run the function
         * @param func The function to run
         */
        fun LoopTimeController.every(period: Int, func: Runnable) {
            if (this.loops % period == 0) {
                func.run()
            }
        }
    }
}