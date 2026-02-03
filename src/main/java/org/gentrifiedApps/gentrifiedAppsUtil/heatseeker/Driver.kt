package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Vector
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DrivePowerConstraint
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DriveVelocities
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.motor.MotorExtensions.Companion.resetMotor
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.Drawer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.TelemetryMaker
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathVector
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.Localizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DcMotorW
import org.gentrifiedApps.gentrifiedAppsUtil.teleopTracker.MovementData


enum class DRIVETYPE {
    MECANUM
}

class Driver @JvmOverloads constructor(
) {
    private var flName: String? = null;
    private var frName: String? = null;
    private var blName: String? = null;
    private var brName: String? = null;
    private var blDirection: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
    private var frDirection: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
    private var flDirection: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
    private var brDirection: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
    private var zeroPowerBehavior: DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE


    fun setFrontLeftName(name: String): Driver {
        flName = name
        return this
    }

    fun setFrontRightName(name: String): Driver {
        frName = name
        return this
    }

    fun setBackLeftName(name: String): Driver {
        blName = name
        return this
    }

    fun setBackRightName(name: String): Driver {
        brName = name
        return this
    }

    fun reverseBackLeft(): Driver {
        blDirection = DcMotorSimple.Direction.REVERSE
        return this
    }

    fun reverseBackRight(): Driver {
        brDirection = DcMotorSimple.Direction.REVERSE
        return this
    }

    fun reverseFrontLeft(): Driver {
        flDirection = DcMotorSimple.Direction.REVERSE
        return this
    }

    fun reverseFrontRight(): Driver {
        frDirection = DcMotorSimple.Direction.REVERSE
        return this
    }

    fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior): Driver {
        zeroPowerBehavior = behavior
        return this
    }
    /////// above require NO HW MAP


    private var localizer: Localizer? = null
        set(value) {
            field = value
            if (value != null) {
                value.initLocalizer()
                drawer.drawLocalization(value.getPose())
                opMode!!.telemetry.addData("pose", value.getPose().toString())
//                telemetry.sendTelemetryNoUpdate(opMode!!.telemetry, value.getPose())
            }
        }

    fun getAbsPositions(): DriveVelocities {
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

    private var drawer = Drawer()
    fun attachDrawer(drawer: Drawer): Driver {
        this.drawer = drawer
        return this
    }

    var opMode: OpMode? = null
    private var hwMap: HardwareMap? = null

    internal lateinit var fl: DcMotor
    internal lateinit var fr: DcMotor
    internal lateinit var bl: DcMotor
    internal lateinit var br: DcMotor

    private val telemetry: TelemetryMaker = TelemetryMaker()


    fun build(opMode: OpMode) { // change to OpMode
        this.opMode = opMode
        this.hwMap = opMode.hardwareMap
//        telemetry.attach(opMode.telemetry)
        verifyRequirements()
        initialize()
    }

    private fun verifyRequirements() {
        val check1 = flName != null && frName != null && blName != null && brName != null

        val final = check1
        if (!final) {
            throw Exception("Requirements not met")
        }
    }

    private fun initialize() {
        fl = hwMap!!.get(DcMotorW::class.java, flName!!)
        fr = hwMap!!.get(DcMotorW::class.java, frName!!)
        bl = hwMap!!.get(DcMotorW::class.java, blName!!)
        br = hwMap!!.get(DcMotorW::class.java, brName!!)
        resetDriveEncoders()
        fl.zeroPowerBehavior = zeroPowerBehavior
        fr.zeroPowerBehavior = zeroPowerBehavior
        bl.zeroPowerBehavior = zeroPowerBehavior
        br.zeroPowerBehavior = zeroPowerBehavior
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

    internal fun updatePoseEstimate() {
        localizer?.update()
    }

    fun getCurrentPose(): Target2D {
        return localizer!!.getPose()
    }

    fun setWheelPower(powerCoefficients: DrivePowerCoefficients) {
        fl.power = powerCoefficients.frontLeft
        fr.power = powerCoefficients.frontRight
        bl.power = powerCoefficients.backLeft
        br.power = powerCoefficients.backRight
    }


    fun sendEncoders(): List<Pair<DcMotor, String?>> {
//        verifyRequirements()
        return listOf(Pair(fl, flName), Pair(fr, frName), Pair(bl, blName), Pair(br, brName))
    }


    internal fun findWheelVectors(data: MovementData): DrivePowerCoefficients {
        return findWheelVectors(data.y, data.x, data.rotation)
    }

    fun findWheelVectors(fwd: Double, strafe: Double, turn: Double): DrivePowerCoefficients {
        val frontLeft = fwd + strafe + turn
        val frontRight = fwd - strafe - turn
        val backLeft = fwd - strafe + turn
        val backRight = fwd + strafe - turn
        return DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
    }

    fun findWheelVectors(v: PathVector): DrivePowerCoefficients { // wrong this is [0..1]
        val fwd = v.translationalVector.a
        val strafe = v.translationalVector.b
        val turn = v.rotationalVector.angle
        return findWheelVectors(fwd, strafe, turn)
    }

    fun addDriftCorrection(constraint: DrivePowerConstraint): Driver {
        driftCoefficients = constraint
        return this
    }

    fun aconstructor(flObj: DcMotor, frObj: DcMotor, blObj: DcMotor, brObj: DcMotor): Driver {
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

        fun findWheelVecs(v: Vector): DrivePowerCoefficients {
            val fwd = v.b
            val strafe = v.a
            val turn = v.c
            return Driver.Companion.findWheelVectors(fwd, strafe, turn)
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