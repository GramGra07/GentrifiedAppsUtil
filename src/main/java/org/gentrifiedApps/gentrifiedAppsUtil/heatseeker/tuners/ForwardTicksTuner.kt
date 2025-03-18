package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

class ForwardTicksTuner(private val driver: Driver) : LinearOpMode() {
    val distance = 48
    override fun runOpMode() {
        Scribe.instance.startLogger("ForwardTicksTuner")
        telemetry.addLine("Move forward 48 inches")
        telemetry.update()
        require(driver.localizer != null)
        waitForStart()
        while (opModeIsActive()) {
            driver.updateNoTelemetry()
            val multiplier = (driver.localizer!!.getPose().y / distance)
            telemetry.addData("Multiply Ticks Per in = ", multiplier)
            telemetry.update()
            Scribe.instance.setSet("HS-FTT").logData("Forward Ticks Mult: $multiplier")
        }
    }
}