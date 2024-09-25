package org.firstinspires.ftc.teamcode.customHardware.loopTime

import org.firstinspires.ftc.robotcore.external.Telemetry

class PeriodicLoopTimeObject(val name: String, everyLoopNum: Int, private val func: Runnable) {
    private val interval: Int = everyLoopNum
    private var refreshRate: Double = 0.0
    private var pastRefreshRate: Double = refreshRate
    private var realRefreshRate: Double = 0.0
    private var pastTimeRR: Double = 0.0

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