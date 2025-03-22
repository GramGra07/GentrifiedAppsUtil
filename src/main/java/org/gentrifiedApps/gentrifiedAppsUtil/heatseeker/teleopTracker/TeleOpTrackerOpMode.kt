package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.teleopTracker

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.gentrifiedApps.gentrifiedAppsUtil.drive.MecanumDriver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.idler.Idler
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController


class TeleOpTrackerOpMode(val name: String,val driver: Driver) :LinearOpMode() {

    enum class State {
        TRACKER,
        GET_DATA,
        PAUSE,
        PREEMPTIVE,
        ENDED
    }
    var state = State.PREEMPTIVE
    val trackerTime = 30.0
    val preemptiveTime = 20.0

    val tracker = OpModeTracker(name)
    val preemptiveLogger = PreemptiveLogger(name)
    val ltc = LoopTimeController()
    val elapsedTime: ElapsedTime = ElapsedTime()
    override fun runOpMode() {
        driver.setOpMode(this)
        tracker.init()
        waitForStart()
        preemptiveLogger.start()
        elapsedTime.reset()
        ltc.reset()
        while (opModeIsActive()) {
            if (state == State.PREEMPTIVE) {
                preemptiveLogger.update(telemetry)
                telemetry.update()
                if (elapsedTime.seconds() > preemptiveTime) {
                    state = State.GET_DATA
                }
            }else if (state == State.GET_DATA){
                preemptiveLogger.end()
                preemptiveLogger.verify(ltc)
                tracker.buildConfigFile(preemptiveLogger.returnData())
                state = State.PAUSE
            }else if (state == State.PAUSE){
                telemetry.addData("Paused","Please press x or square to continue")
                telemetry.update()
                if (gamepad1.x) {
                    state = State.TRACKER
                }
            }else if (state == State.TRACKER){
                elapsedTime.reset()
                val x = gamepad1.left_stick_x
                val y = gamepad1.left_stick_y
                val r = gamepad1.right_stick_x
                val powerCoefficients = MecanumDriver.driveMecanum(x,y,r)
                driver.setWheelPower(powerCoefficients)
                tracker.writeToFile(MovementData(x,y,r))
                if (elapsedTime.seconds() > trackerTime) {
                    state = State.ENDED
                }
            }else if (state == State.ENDED){
                Idler(this).safeIdle(5.0){
                    telemetry.addData("Ended","Saved to file, please run the runner to test it out!")
                    telemetry.update()
                }
                stop()
            }
        }
    }

}