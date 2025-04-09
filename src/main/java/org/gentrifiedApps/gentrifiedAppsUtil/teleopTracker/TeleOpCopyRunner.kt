package org.gentrifiedApps.gentrifiedAppsUtil.teleopTracker

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.idler.Idler
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController

class TeleOpCopyRunner(val name: String, val driver: Driver) : LinearOpMode() {

    val trackedRunner = TrackedRunner(name)
    val ltc: LoopTimeController = LoopTimeController()
    override fun runOpMode() {
        driver.setupOpMode(this)
        waitForStart()
        trackedRunner.init()
        ltc.reset()
        while (opModeIsActive()) {
            val movementData = trackedRunner.iterate()
            telemetry.addData("MovementData", movementData)
            ltc.update()
            ltc.telemetry(telemetry)
            if (movementData != null) {
                driver.setWheelPower(driver.findWheelVectors(movementData))
                Idler(this).safeIdle(movementData.time.toDouble() / 1000) {
                    telemetry.addLine("Running")
                }
            } else {
                driver.setWheelPower(DrivePowerCoefficients.zeros())
            }
            telemetry.update()
        }
    }
}