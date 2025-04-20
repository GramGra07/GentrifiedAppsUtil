package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DrivePowerConstraint
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DriveVelocities
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.Drawer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.TelemetryMaker
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.Localizer
import org.gentrifiedApps.gentrifiedAppsUtil.teleopTracker.MovementData
import kotlin.math.abs


enum class DRIVETYPE {
    MECANUM
}

class Driver @JvmOverloads constructor(
    var opMode: LinearOpMode?,
    val flName: String,
    val frName: String,
    val blName: String,
    val brName: String,
    private val flDirection: Direction = Direction.FORWARD,
    private val frDirection: Direction = Direction.FORWARD,
    private val blDirection: Direction = Direction.FORWARD,
    private val brDirection: Direction = Direction.FORWARD,
) {
    constructor(
        flName: String,
        frName: String,
        blName: String,
        brName: String,
        flDirection: Direction,
        frDirection: Direction,
        blDirection: Direction,
        brDirection: Direction
    ) : this(
        null,
        flName,
        frName,
        blName,
        brName,
        flDirection,
        frDirection,
        blDirection,
        brDirection
    )

    var localizer: Localizer? = null
        set(value) {
            field = value
            if (value != null) {
                value.initLocalizer()
                drawer.drawLocalization(value.getPose())
                telemetry.sendTelemetryNoUpdate(opMode!!.telemetry, value.getPose())
            }
        }

    internal fun getPositions() : DriveVelocities{
        return DriveVelocities(
            abs(fl.currentPosition),
            abs(fr.currentPosition),
            abs(bl.currentPosition),
            abs(br.currentPosition)
        )
    }

    val driveType = DRIVETYPE.MECANUM
    lateinit var hwMap: HardwareMap
    private lateinit var fl: DcMotor
    private lateinit var fr: DcMotor
    private lateinit var bl: DcMotor
    private lateinit var br: DcMotor

    val drawer = Drawer()
    private val telemetry: TelemetryMaker = TelemetryMaker()

    init {
        if (opMode != null) {
            initialize()
        }
    }

    fun setupOpMode(opMode: LinearOpMode) {
        this.opMode = opMode
        initialize()
    }

    private fun initialize() {
        hwMap = opMode!!.hardwareMap
        fl = hwMap.get(DcMotor::class.java, flName)
        fr = hwMap.get(DcMotor::class.java, frName)
        bl = hwMap.get(DcMotor::class.java, blName)
        br = hwMap.get(DcMotor::class.java, brName)
        fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        fr.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        bl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        br.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        fl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        fr.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        bl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        br.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        fl.direction = flDirection
        fr.direction = frDirection
        bl.direction = blDirection
        br.direction = brDirection

        if (localizer != null) {
            localizer!!.initLocalizer()
            drawer.drawLocalization(localizer!!.getPose())
            telemetry.sendTelemetryNoUpdate(opMode!!.telemetry, localizer!!.getPose())
        }
    }

    fun update() {
        updatePoseEstimate()
        if (localizer != null) {
            drawer.drawLocalization(localizer!!.getPose())
            telemetry.sendTelemetryNoUpdate(opMode!!.telemetry, localizer!!.getPose())
        }
    }

    fun updateNoTelemetry() {
        updatePoseEstimate()
        if (localizer != null) {
            drawer.drawLocalization(localizer!!.getPose())
        }
    }

    private fun updatePoseEstimate() {
        localizer?.update()
    }

    fun findWheelVectors(data: MovementData): DrivePowerCoefficients {
        return findWheelVectorsXY(data.x, data.y, data.rotation)
    }

    fun findWheelVectorsXY(x: Double, y: Double, rotation: Double): DrivePowerCoefficients {
        return findWheelVectors(y, x, rotation)
    }

    fun findWheelVectors(fwd: Double, strafe: Double, turn: Double): DrivePowerCoefficients {
        val frontLeft = fwd + strafe + turn
        val frontRight = fwd - strafe - turn
        val backLeft = fwd - strafe + turn
        val backRight = fwd + strafe - turn
        return DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
    }

    fun setWheelPower(powerCoefficients: DrivePowerCoefficients) {
        fl.power = powerCoefficients.frontLeft
        fr.power = powerCoefficients.frontRight
        bl.power = powerCoefficients.backLeft
        br.power = powerCoefficients.backRight
    }


    fun sendEncoders(): List<Pair<DcMotor, String>> {
        return listOf(Pair(fl, flName), Pair(fr, frName), Pair(bl, blName), Pair(br, brName))
    }

    fun addDriftCorrection(constraint: DrivePowerConstraint): Driver {
        driftCoefficients = constraint
        return this
    }
    companion object {

        var driftCoefficients: DrivePowerConstraint? = null
        @JvmStatic fun applyDriftCorrection(coefficients: DrivePowerCoefficients): DrivePowerCoefficients {
            return if (driftCoefficients != null) {
                coefficients.applyConstraint(driftCoefficients!!)
            } else {
                coefficients
            }
        }
        fun findWheelVectors(fwd: Double, strafe: Double, turn: Double): DrivePowerCoefficients {
            val frontLeft = fwd + strafe + turn
            val frontRight = fwd - strafe - turn
            val backLeft = fwd - strafe + turn
            val backRight = fwd + strafe - turn

            return DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
        }
    }
}