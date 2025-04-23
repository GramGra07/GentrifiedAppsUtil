package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.tracking

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.Encoder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.EncoderStorage
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.TrackingLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import kotlin.math.cos
import kotlin.math.sin

data class IMUParams(val name: String, val params: IMU.Parameters) {
    fun initialize(hwMap: HardwareMap): IMU {
        val imu = hwMap.get(IMU::class.java, name)
        imu.initialize(params)
        imu.resetYaw()
        return imu
    }
}

class TwoWheelLocalizer(
    private val ahwMap: HardwareMap,
    private val startPose: Target2D,
    twoEncoder: EncoderStorage,
    private val imuParams: IMUParams
) : TrackingLocalizer() {
    private var parEncoder: Encoder = twoEncoder.par1Encoder
    private var perpEncoder: Encoder = twoEncoder.perpEncoder
    private lateinit var imu: IMU
    private var pose: Target2D = Target2D(0.0, 0.0, Angle.blank())
    override fun initLocalizer() {
        imu = imuParams.initialize(ahwMap)
        parEncoder.reset()
        perpEncoder.reset()
        setPose(startPose)
    }

    override fun update() {
        val deltaTheta = imu.robotYawPitchRollAngles.yaw - pose.angle.toRadians()
        val deltaPar =
            (parEncoder.getDelta() / parEncoder.ticksPerIn()) - (deltaTheta * parEncoder.offset)
        val deltaPerp =
            (perpEncoder.getDelta() / parEncoder.ticksPerIn()) - (deltaTheta * perpEncoder.offset)

        val deltaX = deltaPar * cos(deltaTheta) - deltaPerp * sin(deltaTheta)
        val deltaY = deltaPar * sin(deltaTheta) + deltaPerp * cos(deltaTheta)

        val xNew = pose.x + deltaX
        val yNew = pose.y + deltaY
        val hNew = pose.h() + deltaTheta

        setPose(Target2D(xNew, yNew, Angle(hNew)))

        parEncoder.setLastPosition()
        perpEncoder.setLastPosition()
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
        telemetry.addData("Par Encoder", parEncoder.getTicks())
        telemetry.addData("Perp Encoder", perpEncoder.getTicks())
        telemetry.update()
    }

}