package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.OutputFormatter

class TelemetryW {
    constructor() {
    }

    private var toLog: ArrayList<String> = arrayListOf()
    fun addData(caption: String, value: Any) {

        toLog.add("$caption: $value")
    }

    fun update() {
        for (i in toLog.indices) {
            OutputFormatter.instance.sendData("TelemetryW", toLog[i])
        }
        toLog.clear()
    }

    fun verify(n: Int): Boolean = toLog.size == n
}