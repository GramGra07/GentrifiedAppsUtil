package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DrivePowerConstraint
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DriveVelocities
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.motor.MotorExtensions.Companion.resetMotor
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.Drawer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.TelemetryMaker
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.Localizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.LinearOpModeW
import org.gentrifiedApps.gentrifiedAppsUtil.teleopTracker.MovementData


enum class DRIVETYPE {
    MECANUM
}

class DriverW @JvmOverloads constructor(
    var opMode: LinearOpModeW?,

    val flName: String,
    val frName: String,
    val blName: String,
    val brName: String,
    private val flDirection: Direction = Direction.FORWARD,
    private val frDirection: Direction = Direction.FORWARD,
    private val blDirection: Direction = Direction.FORWARD,
    private val brDirection: Direction = Direction.FORWARD,
) {
    constructor() : this(null, "", "", "", "")
    constructor(
        flName: String,
        frName: String,
        blName: String,
        brName: String
    ) : this(
        flName,
        frName,
        blName,
        brName,
        Direction.FORWARD,
        Direction.FORWARD,
        Direction.FORWARD,
        Direction.FORWARD
    )

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

    internal var localizer: Localizer? = null
        set(value) {
            field = value
            if (value != null) {
                value.initLocalizer()
                drawer.drawLocalization(value.getPose())
                opMode!!.telemetry.addData("pose", value.getPose().toString())
//                telemetry.sendTelemetryNoUpdate(opMode!!.telemetry, value.getPose())
            }
        }

    internal fun getAbsPositions(): DriveVelocities {
        return getPositions().abs()
    }

    fun getPositions(): DriveVelocities {
        return DriveVelocities(
            fl.currentPosition,
            fr.currentPosition,
            bl.currentPosition,
            br.currentPosition
        )
    }

    fun resetDriveEncoders() {
        resetMotor(fl, DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        resetMotor(fr, DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        resetMotor(bl, DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        resetMotor(br, DcMotor.RunMode.RUN_WITHOUT_ENCODER)
    }

    fun showEncoderPositions(telemetry: Telemetry) {
        telemetry.addData("FL", fl.currentPosition)
        telemetry.addData("FR", fr.currentPosition)
        telemetry.addData("BL", bl.currentPosition)
        telemetry.addData("BR", br.currentPosition)
    }

    public var hwMap: HWMapW? = null
    lateinit var fl: DcMotorW
    lateinit var fr: DcMotorW
    lateinit var bl: DcMotorW
    lateinit var br: DcMotorW

    val drawer = Drawer()
    private val telemetry: TelemetryMaker = TelemetryMaker()

    init {
        if (opMode != null) {
            initialize()
        }
    }

    fun setupOpMode(opMode: LinearOpModeW) {
        this.opMode = opMode
        initialize()
    }

    private fun initialize() {
        hwMap = opMode!!.hwMap
        fl = hwMap!!.get(DcMotorW::class.java, flName, 1)
        fr = hwMap!!.get(DcMotorW::class.java, frName, 2)
        bl = hwMap!!.get(DcMotorW::class.java, blName, 3)
        br = hwMap!!.get(DcMotorW::class.java, brName, 4)
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
            opMode!!.telemetry.addData("pose", localizer!!.getPose().toString())
//            telemetry.sendTelemetryNoUpdate(opMode!!.telemetry, localizer!!.getPose())
        }
    }

    internal fun update() {
        updatePoseEstimate()
        if (localizer != null) {
            drawer.drawLocalization(localizer!!.getPose())
            opMode!!.telemetry.addData("pose", localizer!!.getPose().toString())
//            telemetry.sendTelemetryNoUpdate(opMode!!.telemetry, localizer!!.getPose())
        }
    }

    internal fun updateNoTelemetry() {
        updatePoseEstimate()
        if (localizer != null) {
            drawer.drawLocalization(localizer!!.getPose())
        }
    }

    internal fun updatePoseEstimate() {
        localizer?.update()
    }

    fun getCurrentPose(): Target2D {
        return localizer!!.getPose()
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

    fun addDriftCorrection(constraint: DrivePowerConstraint): DriverW {
        driftCoefficients = constraint
        return this
    }

    fun aconstructor(flObj: DcMotorW, frObj: DcMotorW, blObj: DcMotorW, brObj: DcMotorW): DriverW {
        fl = flObj
        fr = frObj
        bl = blObj
        br = brObj
        return this
    }


    companion object {

        var driftCoefficients: DrivePowerConstraint? = null

        @JvmStatic
        fun applyDriftCorrection(coefficients: DrivePowerCoefficients): DrivePowerCoefficients {
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