package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.teleopTracker

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.drive.MecanumDriver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.idler.Idler
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController


class TeleOpTrackerOpMode(val name: String, val driver: Driver) : LinearOpMode() {

    enum class State {
        TRACKER,
        PAUSE,
        ENDED
    }

    var state = State.PAUSE
    val trackerTime = 30.0

    val tracker = OpModeTracker(name)
    val ltc = LoopTimeController()
    val elapsedTime: ElapsedTime = ElapsedTime()
    override fun runOpMode() {
        driver.setupOpMode(this)
        tracker.init()
        waitForStart()
        elapsedTime.reset()
        ltc.reset()
        while (opModeIsActive()) {
            if (state == State.PAUSE) {
                telemetry.addData("Paused", "Please press x or square to continue")
                telemetry.update()
                if (gamepad1.x) {
                    state = State.TRACKER
                    elapsedTime.reset()
                }
            } else if (state == State.TRACKER) {
                val x = gamepad1.left_stick_x
                val y = gamepad1.left_stick_y
                val r = gamepad1.right_stick_x
                val powerCoefficients = MecanumDriver.driveMecanum(x, y, r)
                driver.setWheelPower(powerCoefficients)
                tracker.writeToFile(MovementData(x, y, r))
                if (elapsedTime.seconds() > trackerTime) {
                    state = State.ENDED
                }
                telemetry.addData("Running", "")
                telemetry.update()
            } else if (state == State.ENDED) {
                driver.setWheelPower(DrivePowerCoefficients.zeros())
                Idler(this).safeIdle(5.0) {
                    telemetry.addData(
                        "Ended",
                        "Saved to file, please run the runner to test it out!"
                    )
                    telemetry.update()
                }
                terminateOpModeNow()
                state = State.PAUSE
            }
        }
    }

}