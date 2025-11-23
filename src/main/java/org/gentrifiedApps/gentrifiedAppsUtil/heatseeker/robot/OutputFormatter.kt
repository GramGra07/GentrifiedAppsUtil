package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot

internal class OutputFormatter {
    companion object {

        var instance: OutputFormatter = OutputFormatter()
    }

    fun getOutputFormatter(): OutputFormatter {
        return instance
    }

    fun sendData(caption: String, text: String) {
        println("[$caption] $text")
    }
}