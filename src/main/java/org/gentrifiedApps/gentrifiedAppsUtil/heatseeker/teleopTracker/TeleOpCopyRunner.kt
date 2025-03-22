package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.teleopTracker

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController

class TeleOpCopyRunner(val name: String,val driver: Driver): LinearOpMode() {

    val trackedRunner = TrackedRunner(name)
    val ltc: LoopTimeController = LoopTimeController()
    override fun runOpMode() {
        driver.setupOpMode(this)
        waitForStart()
        ltc.reset()
        while (opModeIsActive()) {
            val movementData = trackedRunner.iterate()
            if (movementData!= null) {
                ltc.update()
                ltc.telemetry(telemetry)
                telemetry.update()

                driver.setWheelPower(driver.findWheelVectors(movementData))
            }
        }
    }
}