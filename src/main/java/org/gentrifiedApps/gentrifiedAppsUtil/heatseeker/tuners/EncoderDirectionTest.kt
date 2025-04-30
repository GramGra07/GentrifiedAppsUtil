package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver


class EncoderDirectionTest(val driver: Driver) : LinearOpMode() {
    override fun runOpMode() {
        Scribe.instance.startLogger("EncoderDirectionTest")
//        require(driver.localizer != null)
//        require(driver.localizer is TrackingLocalizer || driver.localizer is PinpointLocalizer)
//        waitForStart()
//        while (opModeIsActive()) {
//            driver.localizer!!.testEncoderDirection(telemetry)
//        }
    }
}