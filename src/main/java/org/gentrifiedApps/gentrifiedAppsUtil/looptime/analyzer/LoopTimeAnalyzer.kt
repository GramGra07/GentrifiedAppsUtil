package org.gentrifiedApps.gentrifiedAppsUtil.looptime.analyzer

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController
import org.jetbrains.annotations.TestOnly

/**
 * A class to analyze the time it takes to run functions.
 * @param opMode The opmode to use
 * @param testingLoops The amount of loops to test
 * @param initLoop The loop to run before testing
 * @param allFunctions All the functions to test
 */
open class LoopTimeAnalyzer(
    private val opMode: OpMode?,
    val testingLoops: Double, val initLoop: Runnable, val allFunctions: List<TestableFunctions>
) {
    var loopTimeController: LoopTimeController = LoopTimeController()
    fun init() {
        initLoop.run()
    }

    fun testEach() {
        allFunctions.forEach { function ->
            for (i in 0 until testingLoops.toInt()) {
                loopTimeController.update()
                function.run()
                function.addTime(loopTimeController.deltaTime)
            }
            function.analyze()
            if (opMode != null) function.telemetry(opMode.telemetry) else function.print()
            loopTimeController.deltaTime = 0.0
        }
    }
}

/**
 * A class to test functions.
 * @param name The name of the function
 * @param function The function to test
 * @property allTimes All the times the function took to run
 * @property averageTime The average time the function took to run
 * @see LoopTimeAnalyzer
 */
class TestableFunctions(val name: String, val function: Runnable) {
    var allTimes: MutableList<Double> = mutableListOf()
    var averageTime: Double = 0.0

    fun run() {
        function.run()
    }

    fun addTime(time: Double) {
        allTimes.add(time)
    }

    fun analyze() {
        averageTime = allTimes.average()
    }

    /**
     * Telemetry for the function
     * @param telemetry The telemetry to use, opmode.telemetry
     */
    fun telemetry(telemetry: Telemetry) {
        telemetry.addData(name, averageTime)
    }

    @TestOnly
    fun print() {
        println("$name: $averageTime")
    }
}