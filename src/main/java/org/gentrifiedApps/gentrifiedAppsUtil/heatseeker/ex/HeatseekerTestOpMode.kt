package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.ex

import com.qualcomm.hardware.sparkfun.SparkFunOTOS.Pose2D
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Heatseeker
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.AngleUnit
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.OTOSLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.SparkFunOTOSParams

@TeleOp
@Disabled
class HeatseekerTestOpMode : LinearOpMode() {
    private val otos: OTOSLocalizer = OTOSLocalizer(
        "spark", this.hardwareMap, Target2D.blank(),
        SparkFunOTOSParams(Pose2D(1.0, 2.0, Math.toRadians(90.0)))
    )
    private val driver = Driver(this, "fl", "fr", "bl", "br", otos)
    private val heatseeker = Heatseeker(driver)

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