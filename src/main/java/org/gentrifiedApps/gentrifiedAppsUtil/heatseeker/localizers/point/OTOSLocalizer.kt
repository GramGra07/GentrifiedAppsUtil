package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.hardware.sparkfun.SparkFunOTOS.Pose2D
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.PointLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D

data class SparkFunOTOSParams(
    val offset: Pose2D,
    val angularScalar: Double = 1.0,
    val linearScalar: Double = 1.0,
) {
    fun initialize(spark: SparkFunOTOS) {
        spark.initialize()
        spark.offset = offset
        spark.linearUnit = DistanceUnit.INCH
        spark.angularUnit = AngleUnit.RADIANS
        spark.angularScalar = angularScalar
        spark.linearScalar = linearScalar
        spark.calibrateImu()
        spark.resetTracking()
    }
}

class OTOSLocalizer(
    name: String,
    hwMap: HardwareMap,
    private val startPose: Target2D,
    private val params: SparkFunOTOSParams
) : PointLocalizer() {
    private var pose: Target2D = Target2D(0.0, 0.0, Angle.blank())
    private var spark: SparkFunOTOS = hwMap.get(SparkFunOTOS::class.java, name)

    override fun initLocalizer() {
        params.initialize(spark)
        setPose(startPose)
        spark.begin()
    }

    override fun update() {
        this.pose = spark.position.toTarget2D()
    }

    override fun getPose(): Target2D {
        return this.pose
    }

    override fun setPose(pose: Target2D) {
        this.pose = pose
        spark.position = pose.toPose2D()
    }

    override fun getPoseError(pose: Target2D): Target2D {
        return Target2D(
            pose.x - this.pose.x,
            pose.y - this.pose.y,
            Angle(pose.angle.toRadians() - this.pose.angle.toRadians())
        )
    }

    override fun testEncoderDirection(telemetry: Telemetry) {

    }

    private fun Pose2D.toTarget2D(): Target2D {
        return Target2D(
            this.x,
            this.y,
            Angle(
                this.h,
                org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.AngleUnit.RADIANS
            )
        )
    }
}