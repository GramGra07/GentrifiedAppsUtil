package org.gentrifiedApps.gentrifiedAppsUtil.hardware.servo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

class ServoTesterOpMode @JvmOverloads constructor(
    val servoName: String,
    val positions: List<Double> = listOf(
        0.0,
        90.0,
        180.0,
    )
) : LinearOpMode() {

    var index = 0
    override fun runOpMode() {
        Scribe.instance.startLogger(this)
        val servo = ServoPlus(hardwareMap, servoName)
        val gamepad = (gamepad1)
        servo.position = positions[index]
        waitForStart()
        while (opModeIsActive()) {
            telemetry.addLine("Press gamepad1.a or CROSS to change servo position")
            if (gamepad.aWasPressed()) {
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