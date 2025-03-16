package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver


class LocalizerTest(val driver: Driver) : LinearOpMode() {
    override fun runOpMode() {
        Scribe.instance.startLogger("LocalizerTest")
        require(driver.localizer != null)
        waitForStart()
        while (opModeIsActive()) {
            driver.update()
        }
    }
}