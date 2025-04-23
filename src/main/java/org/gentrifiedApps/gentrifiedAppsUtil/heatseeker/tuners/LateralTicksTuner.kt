package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

class LateralTicksTuner(private val driver: Driver) : LinearOpMode() {
    val distance = 48
    override fun runOpMode() {
        Scribe.instance.startLogger("LateralTicksTuner")
        require(driver.localizer != null)
        telemetry.addLine("Move laterally 48 inches")
        telemetry.update()

        waitForStart()
        while (opModeIsActive()) {
            driver.updateNoTelemetry()
            val mult = (driver.localizer!!.getPose().x / distance)
            telemetry.addData("Multiply Ticks Per in = ", mult)
            telemetry.update()
            Scribe.instance.setSet("HS-LTT").logData("Lateral Ticks Mult: $mult")
        }
    }
}