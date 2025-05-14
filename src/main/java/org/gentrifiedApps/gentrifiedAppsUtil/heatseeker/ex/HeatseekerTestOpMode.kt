package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.ex

import com.qualcomm.hardware.sparkfun.SparkFunOTOS.Pose2D
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.OTOSLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.SparkFunOTOSParams

@Autonomous
@Disabled
class HeatseekerTestOpMode : LinearOpMode() {

    override fun runOpMode() {
        OTOSLocalizer(
            "spark", this.hardwareMap, Target2D.blank(),
            SparkFunOTOSParams(Pose2D(1.0, 2.0, Math.toRadians(90.0)))
        )
//        val driver = Driver(
//            this,
//            "fl",
//            "fr",
//            "bl",
//            "br",
//            DcMotorSimple.Direction.FORWARD,
//            DcMotorSimple.Direction.REVERSE,
//            DcMotorSimple.Direction.FORWARD,
//            DcMotorSimple.Direction.FORWARD,
//        )
//        driver.localizer =
//            otos
//        val heatseeker = Heatseeker(
//            driver,
//            ErrorPIDController(1.0, 0.0, 0.0),
//            ErrorPIDController(1.0, 0.0, 0.0),
//            ErrorPIDController(1.0, 0.0, 0.0)
//        )
//
//        val path = PathBuilder()
//            .addWaypoint(Waypoint(1.0, 2.0, Angle(90.0, AngleUnit.DEGREES), 1.0))
//            .addWaypoint(Waypoint(3.0, 4.0, Angle(90.0, AngleUnit.DEGREES), 1.0))
//            .addWaypoint(Waypoint(5.0, 6.0, Angle(90.0, AngleUnit.DEGREES), 1.0))
//            .build()
//        waitForStart()
//        if (opModeIsActive()) {
//            heatseeker.followPath(path, 1.0)
//        }
    }
}

@TeleOp
@Disabled
class HeatseekerTeleTestOpMode : LinearOpMode() {
    override fun runOpMode() {

//        val otos: OTOSLocalizer = OTOSLocalizer(
//            "spark", this.hardwareMap, Target2D.blank(),
//            SparkFunOTOSParams(Pose2D(1.0, 2.0, Math.toRadians(90.0)))
//        )
//        val driver = Driver(
//            this,
//            "fl",
//            "fr",
//            "bl",
//            "br",
//            DcMotorSimple.Direction.FORWARD,
//            DcMotorSimple.Direction.REVERSE,
//            DcMotorSimple.Direction.FORWARD,
//            DcMotorSimple.Direction.FORWARD,
//        )
//        driver.localizer =
//            otos
//        val heatseeker = Heatseeker(
//            driver,
//            ErrorPIDController(1.0, 0.0, 0.0),
//            ErrorPIDController(1.0, 0.0, 0.0),
//            ErrorPIDController(1.0, 0.0, 0.0)
//        )
//        val teleOpCorrector = heatseeker.teleOpCorrector()
//        waitForStart()
//        while (opModeIsActive()) {
//            var powerCoefficients =
//                driveMecanum(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x)
//            if (!powerCoefficients.notZero()) {
//                // has no powers to move
//                teleOpCorrector.updateOrientation()
//            } else if (gamepad1.right_stick_x.toDouble() == 0.0) {
//                powerCoefficients = teleOpCorrector.correctByAngle(powerCoefficients)
//            }
//
//            driver.setWheelPower(powerCoefficients)
//            driver.update()
//        }
    }
}