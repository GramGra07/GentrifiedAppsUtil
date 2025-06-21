package org.gentrifiedApps.gentrifiedAppsUtil.hardware.servo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.GamepadPlus

class ServoTesterOpMode @JvmOverloads constructor(
    val servoName: String,
    val positions: List<Double> = listOf(
        0.0,
        90.0,
        180.0,
        0.0,
        -90.0,
        -180.0
    )
) : LinearOpMode() {

    var index = 0
    override fun runOpMode() {
        Scribe.instance.startLogger(this)
        val servo = ServoPlus(hardwareMap, servoName)
        val gamepad = GamepadPlus(gamepad1)
        servo.position = positions[index]
        waitForStart()
        while (opModeIsActive()) {
            telemetry.addLine("Press gamepad1.a or CROSS to change servo position")
            if (gamepad.buttonJustPressed(Button.A)) {
                index = (index + 1) % positions.size
                servo.position = positions[index]
                telemetry.addData("Servo Position Changed", "New Position: ${servo.position}")
            } else {
                telemetry.addData("Current Servo Position", servo.position)
            }

            telemetry.addData("Servo Position", servo.position)
            telemetry.update()
            gamepad.sync()
        }
    }


}