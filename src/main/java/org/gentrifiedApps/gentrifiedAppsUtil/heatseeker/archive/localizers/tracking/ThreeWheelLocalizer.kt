package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.archive.localizers.tracking

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.archive.Encoder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.archive.EncoderStorage
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.archive.localizers.TrackingLocalizer
import kotlin.math.cos
import kotlin.math.sin

class ThreeWheelLocalizer(
    threeEncoder: EncoderStorage,
    private val trackWidth: Double,
    private val startPose: Target2D,
) : TrackingLocalizer() {
    private var perpEncoder: Encoder = threeEncoder.perpEncoder
    private var leftEncoder: Encoder = threeEncoder.par1Encoder
    private var rightEncoder: Encoder = threeEncoder.par2Encoder!!

    private var pose: Target2D = Target2D(0.0, 0.0, Angle.blank())

    override fun initLocalizer() {
        perpEncoder.reset()
        leftEncoder.reset()
        rightEncoder.reset()
        setPose(startPose)
    }

    override fun update() {
        val left = leftEncoder.getDelta() / leftEncoder.ticksPerIn()
        val right = rightEncoder.getDelta() / rightEncoder.ticksPerIn()
        val perp = perpEncoder.getDelta() / perpEncoder.ticksPerIn()

        val deltaTheta = (right - left) / trackWidth
        val xLocal = (left + right) / 2
        val yLocal = perp - (deltaTheta * perpEncoder.offset)

        val newX = xLocal * cos(pose.h()) - yLocal * sin(pose.h())
        val newY = xLocal * sin(pose.h()) + yLocal * cos(pose.h())
        val newH = pose.h() + deltaTheta

        setPose(Target2D(newX, newY, Angle(newH)))

        perpEncoder.setLastPosition()
        leftEncoder.setLastPosition()
        rightEncoder.setLastPosition()
    }

    override fun getPose(): Target2D {
        return pose
    }

    override fun setPose(pose: Target2D) {
        this.pose = pose
    }

    override fun getPoseError(pose: Target2D): Target2D {
        return Target2D(
            pose.x - this.pose.x,
            pose.y - this.pose.y,
            Angle(pose.angle.toRadians() - this.pose.angle.toRadians())
        )
    }

    override fun testEncoderDirection(telemetry: Telemetry) {
        telemetry.addData("Left Encoder", leftEncoder.getTicks())
        telemetry.addData("Right Encoder", rightEncoder.getTicks())
        telemetry.addData("Perp Encoder", perpEncoder.getTicks())
        telemetry.update()
    }
}