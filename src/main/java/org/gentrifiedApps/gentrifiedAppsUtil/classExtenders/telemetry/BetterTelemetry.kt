package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.telemetry

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.robotcore.external.Telemetry


class BetterTelemetry @JvmOverloads constructor(var telemetry: Telemetry,val dash: Boolean=false) {
    fun addMultipleTelemetry(){
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
    }

    init {
        if (dash) {
            addMultipleTelemetry()
        }
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML)
        telemetry.msTransmissionInterval = 50
    }
    fun addData(key: String, value: Any) {
        telemetry.addData(key, value)
    }
    fun addLine(value: String) {
        telemetry.addLine(value)
    }
    fun addColoredData(key: String, value: Any, color: String) {
        val builder = StringBuilder()
        builder.append("<font color='$color'> face=monospace")
        builder.append(value)
        builder.append("</font>")
        addData(key, builder.toString())
    }
    fun addColoredKey(key: String, value: Any, color: String) {
        val builder = StringBuilder()
        builder.append("<font color='$color'> face=monospace")
        builder.append(key)
        builder.append("</font>")
        addData(builder.toString(), value)
    }
    fun addColoredKeyValue(key: String, value: Any, color: String) {
        val builder = StringBuilder()
        builder.append("<font color='$color'> face=monospace")
        builder.append(key)
        builder.append(" : ")
        builder.append(value)
        builder.append("</font>")
        addLine(builder.toString())
    }
}