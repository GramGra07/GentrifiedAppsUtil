package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.ex

import com.qualcomm.hardware.sparkfun.SparkFunOTOS.Pose2D
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.drive.MecanumDriver.Companion.driveMecanum
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Heatseeker
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PIDController
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.AngleUnit
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.OTOSLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.SparkFunOTOSParams

@Autonomous
@Disabled
class HeatseekerTestOpMode : LinearOpMode() {
    private val otos: OTOSLocalizer = OTOSLocalizer(
        "spark", this.hardwareMap, Target2D.blank(),
        SparkFunOTOSParams(Pose2D(1.0, 2.0, Math.toRadians(90.0)))
    )
    private val driver = Driver(this, "fl", "fr", "bl", "br",DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE ,DcMotorSimple.Direction.FORWARD,DcMotorSimple.Direction.FORWARD, otos)
    private val heatseeker = Heatseeker(driver, PIDController(1.0, 0.0, 0.0), PIDController(1.0, 0.0, 0.0), PIDController(1.0, 0.0, 0.0))

    private val path = PathBuilder()
        .addWaypoint(Waypoint(1.0, 2.0, Angle(90.0, AngleUnit.DEGREES), 1.0))
        .addWaypoint(Waypoint(3.0, 4.0, Angle(90.0, AngleUnit.DEGREES), 1.0))
        .addWaypoint(Waypoint(5.0, 6.0, Angle(90.0, AngleUnit.DEGREES), 1.0))
        .build()

    override fun runOpMode() {
        if (opModeIsActive()) {
            heatseeker.followPath(path, 1.0)
        }
    }
}

@TeleOp
@Disabled
class HeatseekerTeleTestOpMode : LinearOpMode() {
    private val otos: OTOSLocalizer = OTOSLocalizer(
        "spark", this.hardwareMap, Target2D.blank(),
        SparkFunOTOSParams(Pose2D(1.0, 2.0, Math.toRadians(90.0)))
    )
    private val driver = Driver(this, "fl", "fr", "bl", "br",DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE ,DcMotorSimple.Direction.FORWARD,DcMotorSimple.Direction.FORWARD, otos)
    private val heatseeker = Heatseeker(driver, PIDController(1.0, 0.0, 0.0), PIDController(1.0, 0.0, 0.0), PIDController(1.0, 0.0, 0.0))
    private val teleOpCorrector = heatseeker.teleOpCorrector()

    override fun runOpMode() {
        while (opModeIsActive()) {
            var powerCoefficients = driveMecanum(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x)
            if (!powerCoefficients.notZero()){
                // has no powers to move
                teleOpCorrector.updateOrientation()
            }else if (gamepad1.right_stick_x.toDouble() == 0.0){
                powerCoefficients = teleOpCorrector.correctByAngle(powerCoefficients)
            }

            driver.setWheelPower(powerCoefficients)
            driver.update()
        }
    }
}