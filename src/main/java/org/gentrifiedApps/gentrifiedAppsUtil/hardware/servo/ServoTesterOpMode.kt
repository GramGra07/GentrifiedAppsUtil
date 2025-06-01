package org.gentrifiedApps.gentrifiedAppsUtil.hardware.servo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.GamepadPlus

class ServoTesterOpMode @JvmOverloads constructor(
    val servoName: String,
    val positions: List<Double> = listOf(
        0.0,
        0.5,
        1.0,
        0.0,
        -0.5,
        -1.0
    )
) : LinearOpMode() {
    var index = 0
    override fun runOpMode() {
        val servo = hardwareMap.get(Servo::class.java, servoName)
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
        }
    }


}