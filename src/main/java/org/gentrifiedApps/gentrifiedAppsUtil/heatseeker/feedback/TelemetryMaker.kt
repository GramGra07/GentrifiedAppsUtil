package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D

class TelemetryMaker {
    fun sendTelemetry(telemetry: Telemetry, pose: Target2D, update: Boolean = true) {
        val telemetry2 = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        telemetry2.addData("X", pose.x)
        telemetry2.addData("Y", pose.y)
        telemetry2.addData("H", pose.h())
        if (update) {
            telemetry2.update()
        }
    }

    fun sendTelemetryNoUpdate(telemetry: Telemetry, pose: Target2D) {
        sendTelemetry(telemetry, pose, false)
    }
}