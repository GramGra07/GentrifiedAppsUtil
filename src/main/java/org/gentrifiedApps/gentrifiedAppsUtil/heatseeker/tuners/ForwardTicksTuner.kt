package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

class ForwardTicksTuner(private val driver: Driver) :LinearOpMode() {
    val distance = 48
    override fun runOpMode() {
        telemetry.addLine("Move forward 48 inches")
        telemetry.update()

        waitForStart()
        while (opModeIsActive()) {
            driver.updateNoTelemetry()
            val multiplier = (driver.localizer.getPose().y / distance)
            telemetry.addData("Multiply Ticks Per in = ",multiplier)
            telemetry.update()
        }
    }
}