package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.pinpoint

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Point
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.PointLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.pinpoint.GoBildaPinpointDriver.GoBildaOdometryPods

data class GoBildaPinpointParams(
    val offset: Point, val encoderResolution: GoBildaOdometryPods,
    val e1Direction: GoBildaPinpointDriver.EncoderDirection,
    val e2Direction: GoBildaPinpointDriver.EncoderDirection
) {
    fun initialize(pinpoint: GoBildaPinpointDriver) {
        pinpoint.initialize()
        pinpoint.recalibrateIMU()
        pinpoint.resetPosAndIMU()
        pinpoint.setOffsets(offset.x, offset.y)
        pinpoint.setEncoderResolution(encoderResolution)
        pinpoint.setEncoderDirections(e1Direction, e2Direction)
    }
}

class PinpointLocalizer(
    name: String,
    hwMap: HardwareMap,
    private val startPose: Target2D,
    private val params: GoBildaPinpointParams
) : PointLocalizer() {
    private var pinpoint: GoBildaPinpointDriver = hwMap.get(GoBildaPinpointDriver::class.java, name)
    private var pose: Target2D = Target2D(0.0, 0.0, Angle.blank())

    override fun initLocalizer() {
        params.initialize(pinpoint)
        setPose(startPose)
    }

    override fun update() {
        pinpoint.update()
        this.pose = pinpoint.position.toTarget2D()
    }

    override fun getPose(): Target2D {
        return this.pose
    }

    override fun setPose(pose: Target2D) {
        this.pose = pose
        pinpoint.setPosition(pose.toPose2D2())
    }

    override fun getPoseError(pose: Target2D): Target2D {
        return Target2D(
            pose.x - this.pose.x,
            pose.y - this.pose.y,
            Angle(pose.angle.toRadians() - this.pose.angle.toRadians())
        )
    }

    private fun Pose2D.toTarget2D(): Target2D {
        return Target2D(
            this.getX(DistanceUnit.INCH),
            this.getY(DistanceUnit.INCH),
            Angle(
                this.getHeading(AngleUnit.RADIANS),
                org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.AngleUnit.RADIANS
            )
        )
    }
}