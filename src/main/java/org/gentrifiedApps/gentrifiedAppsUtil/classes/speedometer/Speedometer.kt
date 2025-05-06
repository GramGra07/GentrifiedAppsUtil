package org.gentrifiedApps.gentrifiedAppsUtil.classes.speedometer

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.EncoderSpecs
import kotlin.math.sqrt

abstract class Speedometer(open val opMode: OpMode?) {
    open fun update() {}
    open fun update(point: Point) {}
    abstract var speed: Double
    abstract fun telemetry(telemetry: Telemetry)
    abstract val elapsedTime: ElapsedTime
    abstract var lastTime: Double

    companion object {
        @JvmStatic
        fun newSpeedometer(driver: Driver, encoderSpecs: EncoderSpecs): Speedometer {
            return DriveSpeedometer(driver, encoderSpecs)
        }

        @JvmStatic
        fun newSpeedometer(opMode: OpMode, startPoint: Point): Speedometer {
            return LocalizerSpeedometer(opMode, startPoint)
        }
    }
}

class DriveSpeedometer(val driver: Driver, val encoderSpecs: EncoderSpecs) :
    Speedometer(driver.opMode) {
    override var speed: Double = 0.0
    override val elapsedTime: ElapsedTime = ElapsedTime()
    override var lastTime: Double = 0.0
    override fun telemetry(telemetry: Telemetry) {
        telemetry.addData("Speed", speed)
    }

    var lastPoses = driver.getPositions()
    private fun ticksToInches(ticks: Int): Double {
        return ticks.toDouble() / encoderSpecs.ticksPerInch
    }

    override fun update() {
        speed = (ticksToInches(
            (lastPoses.abs() - driver.getPositions().abs()).average().toInt()
        ) * 0.0254) / (elapsedTime.seconds() - lastTime)
        lastTime = elapsedTime.seconds()
        lastPoses = driver.getPositions()
    }
}

class LocalizerSpeedometer(override val opMode: OpMode, val startPosition: Point) :
    Speedometer(opMode) {
    override var speed: Double = 0.0
    override val elapsedTime: ElapsedTime = ElapsedTime()
    override var lastTime: Double = 0.0
    override fun telemetry(telemetry: Telemetry) {
        telemetry.addData("Speed", speed)
    }

    private var lastPosition = startPosition
    override fun update(point: Point) {
        val distance = sqrt(
            (point.x - lastPosition.x) * (point.x - lastPosition.x) +
                    (point.y - lastPosition.y) * (point.y - lastPosition.y)
        )
        speed = (distance * 0.0254) / elapsedTime.seconds()
        lastPosition = point
    }
}