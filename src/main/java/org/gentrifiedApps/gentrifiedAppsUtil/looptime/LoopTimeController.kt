package org.gentrifiedApps.gentrifiedAppsUtil.looptime

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.objects.PeriodicLoopTimeObject

enum class LTCState {
    INITIALIZING,
    RUNNING
}

/**
 * A class to return loop time information and load periodic loop time objects and .every functions
 * @param periodics The periodic loop time objects to load
 */
open class LoopTimeController(
    private val periodics: List<PeriodicLoopTimeObject?>? = null,
) {
    constructor() : this(null)

    private val timer = ElapsedTime()
    internal var totalLoops: Int = 0
    internal var lps = 0.0
    private var currentTimeS: Double = 0.0
    private var currentTimems: Double = 0.0
    private val calculationTimeDelay: Double = 5.0
    private var hz: Double = 0.0
    private var lastSecondLPast: Double = 0.0
    private var lastSecondL: Double = 0.0
    private var lastSecondTimeDelay: Double = 0.0

    internal var ltcState = LTCState.INITIALIZING


    private var startTime: Double
    private var lastTime: Double
    internal var deltaTime: Double = 0.0

    init {
        timer.reset()
        this.startTime = timer.milliseconds()
        this.lastTime = startTime
        reset()
    }

    fun reset() {
        timer.reset()
        this.startTime = timer.milliseconds()
        this.lastTime = startTime
    }

    private fun doCalculations() {
        lps = totalLoops / (currentTimeS - calculationTimeDelay)
        if (currentTimeS > calculationTimeDelay) {
            totalLoops++
            if (totalLoops == 1) {
                ltcState = LTCState.RUNNING
            }
        }
        if (currentTimeS - 1 > lastSecondTimeDelay) {
            lastSecondL = totalLoops - lastSecondLPast
            hz = lastSecondL / (currentTimeS - lastSecondTimeDelay)
            lastSecondLPast = totalLoops.toDouble()
            lastSecondTimeDelay = currentTimeS
        }
    }

    private var pingTime: Double = 0.0
    private var pingDelay: Double = 30.0

    /**
     * Updates the loop time controller
     */
    fun update() {
        currentTimems = timer.milliseconds()
        currentTimeS = timer.seconds()
        doCalculations()
        periodics?.forEach { obj ->
            obj!!.rr(currentTimeS)
            if (obj.check(totalLoops)) {
                obj.run()
            }
        }
        deltaTime = currentTimems - lastTime
        lastTime = currentTimems
        if (hz < 30 && ltcState == LTCState.RUNNING) {
            if (currentTimeS - pingTime > pingDelay) {
                Scribe.instance.setSet("LTC")
                    .logDebug("Hz dropped past 30, this may cause issues and lag")
                pingTime = currentTimeS
            }
        }
    }

    /**
     * Telemetry for the loop time controller
     * @param telemetry The telemetry to use, opmode.telemetry
     */

    fun telemetry(telemetry: Telemetry) {
        minimalTelemetry(telemetry)
        telemetry.addData("Time Elapsed", "%.1f", deltaTime)
        telemetry.addData("Avg Time Elapsed", "%.1f", (currentTimems - startTime) / totalLoops)
//        telemetry.addData(comparisonDate, "%.1f", comparison-lps)
//        telemetry.addData("lastsecondloops",lastSecondL)
//        telemetry.addData("pastlastsecondloops",lastSecondLPast)
//        telemetry.addData("lastsecondt",lastSecondT)
//        telemetry.addData("Current LPS", "%.1f", lps)
    }

    /**
     * Minimal telemetry for the loop time controller
     * @param telemetry The telemetry to use, opmode.telemetry
     */
    fun minimalTelemetry(telemetry: Telemetry) {
        telemetry.addLine("LOOP TIME")
//        telemetry.addData("Timer", "%.1f", currentTimeS)
//        telemetry.addData("Loops", totalLoops)
        telemetry.addData("Time Elapsed", "%.1f", deltaTime)
//        telemetry.addData("Avg Time Elapsed", "%.1f", (currentTimems - startTime) / totalLoops)
//        telemetry.addData(comparisonDate, "%.1f", comparison-lps)
//        telemetry.addData("lastsecondloops",lastSecondL)
//        telemetry.addData("pastlastsecondloops",lastSecondLPast)
//        telemetry.addData("lastsecondt",lastSecondT)
//        telemetry.addData("Current LPS", "%.1f", lps)
        telemetry.addData("Hz", "%.1f", hz)
    }

    /**
     * A function to run a function every given amount of loops.
     * @param period The amount of loops to wait to run the function
     * @param func The function to run
     */
    fun every(period: Int, func: Runnable) {
        require(period >= 1) { "Period must be greater than 1" }
        if (this.totalLoops % period == 0) {
            func.run()
        }
    }

    /**
     * A function to save your loop times, potentially boosting by 30-40
     * @param hw The hardware map to use
     * @see LynxModule
     */
    fun setLoopSavingCache(hw: HardwareMap): LoopTimeController {
        val allHubs: List<LynxModule> = hw.getAll(LynxModule::class.java)

        for (hub in allHubs) {
            hub.bulkCachingMode = LynxModule.BulkCachingMode.AUTO
        }
        return this
    }
}