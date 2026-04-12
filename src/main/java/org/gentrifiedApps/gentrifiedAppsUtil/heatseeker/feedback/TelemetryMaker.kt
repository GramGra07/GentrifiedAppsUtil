package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DriveVelocities
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D

class TelemetryMaker(private var telemetry: Telemetry) {

    fun showEncoderPositions(positions: DriveVelocities) {
        telemetry.addData("FL", positions.frontLeft)
        telemetry.addData("FR", positions.frontRight)
        telemetry.addData("BL", positions.backLeft)
        telemetry.addData("BR", positions.backRight)
    }

    fun showPose(pose: Target2D) {
//        val telemetry2 = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        telemetry.addData("X", pose.x.toString())
        telemetry.addData("Y", pose.y.toString())
        telemetry.addData("H", pose.h().toString())
    }

    fun update() {
        telemetry.update()
    }
}