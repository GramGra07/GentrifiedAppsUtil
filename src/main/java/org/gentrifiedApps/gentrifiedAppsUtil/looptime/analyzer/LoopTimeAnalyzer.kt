package org.gentrifiedApps.gentrifiedAppsUtil.looptime.analyzer

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController

open class LoopTimeAnalyzer(
    private val opMode: OpMode,
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
            function.telemetry(opMode.telemetry)
            loopTimeController.deltaTime = 0.0
        }
    }
}

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

    fun telemetry(telemetry: Telemetry) {
        telemetry.addData(name, averageTime)
    }
}