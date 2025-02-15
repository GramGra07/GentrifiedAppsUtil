package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

class TrackWidthTuner (private val driver: Driver) : LinearOpMode() {
    val turns = 5
    override fun runOpMode() {
        telemetry.addLine("Turn the robot 360 degrees $turns times")
        telemetry.update()

        waitForStart()
        while (opModeIsActive()) {
            driver.updateNoTelemetry()

            val heading = driver.localizer.getPose().h()
            val correctedTrackWidth = (heading / (2 * Math.PI * turns))
            // Display results
            telemetry.addData("Heading", heading)
            telemetry.addData("Recommended Track Width", correctedTrackWidth)
            telemetry.update()
            telemetry.update()
        }
    }
}