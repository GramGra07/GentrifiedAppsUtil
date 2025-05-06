package org.gentrifiedApps.gentrifiedAppsUtil.classes.odometer

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.EncoderSpecs
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController
import kotlin.math.absoluteValue
import kotlin.math.sqrt

abstract class Odometer(open val opMode: OpMode?) {
    abstract val odometerFileManager: OdometerFileManager
    open fun update() {}
    open fun update(point: Point) {}
    abstract var ODO: Double
    open fun addConstraint(encoderSpecs: EncoderSpecs) {}
    enum class ReturnType {
        RAW_TICKS,
        INCHES,
        METERS,
        MILES;

        fun getUnit(): String {
            return when (this) {
                RAW_TICKS -> "ticks"
                INCHES -> "inches"
                METERS -> "meters"
                MILES -> "miles"
            }
        }
    }

    abstract var returnType: ReturnType
    open var encoderSpecs: EncoderSpecs? = null
    abstract fun telemetry(telemetry: Telemetry)
    abstract fun applyMultiplier(): Double
    open fun switchMult(ticksPerIn: Double?) {}
    open fun switchMult() {}
    abstract fun reset()

    companion object {
        @JvmStatic
        fun newOdometer(driver: Driver): Odometer {
            return DriveOdometer(driver)
        }

        @JvmStatic
        fun newOdometer(opMode: OpMode, startPoint: Point): Odometer {
            return LocalizerOdometer(opMode, startPoint)
        }
    }
}

class DriveOdometer(val driver: Driver) : Odometer(driver.opMode) {
    override val odometerFileManager: OdometerFileManager = OdometerFileManager()
    override var ODO: Double = odometerFileManager.readOdometryData()
    override var encoderSpecs: EncoderSpecs? = odometerFileManager.readConfigData()
    var lastPoses = driver.getPositions()
    override fun reset() {
        ODO = 0.0
        odometerFileManager.writeOdometryData(ODO)
    }

    override fun update() {
        val poses = driver.getPositions()
        ODO += (poses.abs() - lastPoses.abs()).abs().average().absoluteValue
        lastPoses = poses
    }

    fun writeRead(loopTimeController: LoopTimeController, interval: Int) {
        loopTimeController.every(interval) {
            odometerFileManager.writeOdometryData(ODO)
            encoderSpecs = odometerFileManager.readConfigData()
        }
    }

    fun writeRead(loopTimeController: LoopTimeController) {
        loopTimeController.every(1) {
            odometerFileManager.writeOdometryData(ODO)
            encoderSpecs = odometerFileManager.readConfigData()
        }
    }

    override fun addConstraint(encoderSpecs: EncoderSpecs) {
        this.encoderSpecs = encoderSpecs
        odometerFileManager.writeConfigData(encoderSpecs.ticksPerInch.toString())
    }

    override var returnType: ReturnType = ReturnType.RAW_TICKS
    override fun applyMultiplier(): Double {
        if (encoderSpecs == null) {
            return ODO
        }
        return when (returnType) {
            ReturnType.RAW_TICKS -> ODO
            ReturnType.INCHES -> ticksToInches(ODO.toInt())
            ReturnType.METERS -> ticksToInches(ODO.toInt()) * 0.0254
            ReturnType.MILES -> ticksToInches(ODO.toInt()) / (5280 * 12)
        }
    }

    private fun ticksToInches(ticks: Int): Double {
        return ticks.toDouble() / encoderSpecs!!.ticksPerInch
    }

    override fun switchMult() {
        if (encoderSpecs?.ticksPerInch == null) {
            Scribe.instance.setSet("Odometer").logError("Encoder specs not set")
            return
        }
        val inches = ticksToInches(ODO.toInt())
        returnType = when {
            inches > 5280 * 12 -> ReturnType.MILES // 5280 feet in a mile, converted to inches
            inches > 39.37 -> ReturnType.METERS // 1 meter = 39.37 inches
            inches > 2 -> ReturnType.INCHES
            else -> ReturnType.RAW_TICKS
        }
    }

    override fun toString(): String {
        switchMult(encoderSpecs?.ticksPerInch)
        return "DriveOdometer: ${applyMultiplier()} ${returnType.getUnit()}"
    }

    override fun telemetry(telemetry: Telemetry) {
        telemetry.addLine(this.toString())
    }
}

class LocalizerOdometer(override val opMode: OpMode, val startPoint: Point) : Odometer(opMode) {
    override val odometerFileManager: OdometerFileManager = OdometerFileManager()
    override var ODO: Double = odometerFileManager.readOdometryData()
    var lastPoint = startPoint
    override fun reset() {
        ODO = 0.0
        odometerFileManager.writeOdometryData(ODO)
    }

    override fun update(point: Point) {
        // get hypotenuse
        val x = point.x - lastPoint.x
        val y = point.y - lastPoint.y
        val hypotenuse = sqrt(x * x + y * y)
        ODO += hypotenuse.absoluteValue
        lastPoint = point
    }

    fun writeRead(loopTimeController: LoopTimeController, interval: Int) {
        loopTimeController.every(interval) {
            odometerFileManager.writeOdometryData(ODO)
            encoderSpecs = odometerFileManager.readConfigData()
        }
    }

    fun writeRead(loopTimeController: LoopTimeController) {
        loopTimeController.every(1) {
            odometerFileManager.writeOdometryData(ODO)
            encoderSpecs = odometerFileManager.readConfigData()
        }
    }

    override var returnType: ReturnType = ReturnType.INCHES
    override fun applyMultiplier(): Double {
        return when (returnType) {
            ReturnType.INCHES -> ODO
            ReturnType.METERS -> ODO * 0.0254
            ReturnType.MILES -> ODO * 0.0000157828283
            else -> ODO
        }
    }

    override fun switchMult() {
        // already in inches
        returnType = when {
            ODO > 5280 * 12 -> ReturnType.MILES
            ODO > 39.37 -> ReturnType.METERS
            else -> ReturnType.INCHES
        }
    }

    override fun toString(): String {
        return "LocalizerOdometer: ${applyMultiplier()} ${returnType.getUnit()}"
    }

    override fun telemetry(telemetry: Telemetry) {
        telemetry.addLine(this.toString())
    }
}