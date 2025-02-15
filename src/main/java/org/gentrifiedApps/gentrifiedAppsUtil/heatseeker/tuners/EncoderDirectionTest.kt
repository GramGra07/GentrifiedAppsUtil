package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.TrackingLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.pinpoint.PinpointLocalizer


class EncoderDirectionTest(val driver: Driver) :LinearOpMode(){
    override fun runOpMode() {
        require(driver.localizer !=null)
        require(driver.localizer is TrackingLocalizer || driver.localizer is PinpointLocalizer)
        waitForStart()
        while (opModeIsActive()) {
            driver.localizer!!.testEncoderDirection(telemetry)
        }
    }
}