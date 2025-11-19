package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

class TelemetryW {
    constructor() {
        println("TelemetryW booted")
    }

    private var toLog: ArrayList<String> = arrayListOf()
    fun addData(caption: String, value: Object) {

        toLog.plus("$caption: $value")
    }

    fun update() {
        for (log in toLog) {
            println(log)
        }
        toLog.clear()
    }

    fun verify(n: Int): Boolean = toLog.size == n
}