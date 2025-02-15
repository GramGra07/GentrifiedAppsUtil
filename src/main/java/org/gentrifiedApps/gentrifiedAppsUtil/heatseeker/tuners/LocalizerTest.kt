package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.TrackingLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.pinpoint.PinpointLocalizer


class LocalizerTest(val driver: Driver) :LinearOpMode(){
    override fun runOpMode() {
        require(driver.localizer !=null)
        waitForStart()
        while (opModeIsActive()) {
            driver.update()
        }
    }
}