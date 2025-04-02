package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.teleopTracker


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
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
            if (movementData != null) {
                ltc.update()
                ltc.telemetry(telemetry)
                telemetry.update()

                driver.setWheelPower(driver.findWheelVectors(movementData))
            }else{
                driver.setWheelPower(DrivePowerCoefficients.zeros())
            }
            Idler(this).safeIdle(movementData!!.time/1000.0) {
                telemetry.addData("Running", "")
                telemetry.update()
            }
        }
    }
}