package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.tuners

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

enum class MecanumMotors {
    FL, FR, BL, BR, NONE
}


class MecanumDriveDirectionTest : LinearOpMode() {
    val driver: Driver = Driver(
        this,
        "fl",
        "fr",
        "bl",
        "br",
        DcMotorSimple.Direction.FORWARD,
        DcMotorSimple.Direction.FORWARD,
        DcMotorSimple.Direction.FORWARD,
        DcMotorSimple.Direction.FORWARD
    )

    override fun runOpMode() {
        Scribe.instance.startLogger("MecanumDirectionTest")
        var currentlyRunning = MecanumMotors.NONE
        waitForStart()
        while (opModeIsActive()) {
            if (gamepad1.cross) {
                currentlyRunning = MecanumMotors.BR
            } else if (gamepad1.triangle) {
                currentlyRunning = MecanumMotors.FL
            } else if (gamepad1.square) {
                currentlyRunning = MecanumMotors.FR
            } else if (gamepad1.circle) {
                currentlyRunning = MecanumMotors.BL
            }
            telemetry.addLine("Triangle for front left")
            telemetry.addLine("Square for front right")
            telemetry.addLine("Circle for back left")
            telemetry.addLine("X for back right")
            telemetry.addLine("Currently Running: $currentlyRunning")
            telemetry.update()

            when (currentlyRunning) {
                MecanumMotors.FL -> driver.setWheelPower(DrivePowerCoefficients(1.0, 0.0, 0.0, 0.0))
                MecanumMotors.FR -> driver.setWheelPower(DrivePowerCoefficients(0.0, 1.0, 0.0, 0.0))
                MecanumMotors.BL -> driver.setWheelPower(DrivePowerCoefficients(0.0, 0.0, 1.0, 0.0))
                MecanumMotors.BR -> driver.setWheelPower(DrivePowerCoefficients(0.0, 0.0, 0.0, 1.0))
                MecanumMotors.NONE -> driver.setWheelPower(
                    DrivePowerCoefficients(
                        0.0,
                        0.0,
                        0.0,
                        0.0
                    )
                )
            }
        }
    }
}