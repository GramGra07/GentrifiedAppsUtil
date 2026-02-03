package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DrivePowerConstraint
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DriveVelocities
import org.gentrifiedApps.gentrifiedAppsUtil.classes.equations.SlopeIntercept
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.motor.MotorExtensions.Companion.resetMotor
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.Drawer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.TelemetryMaker
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathVector
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.LinearOpModeW
import org.gentrifiedApps.gentrifiedAppsUtil.teleopTracker.MovementData


class DriverW {
    enum class DRIVETYPE {
        MECANUM,
        TANK
    }

    private var drivetrainType: DRIVETYPE = DRIVETYPE.MECANUM

    private var flName: String? = null
    private var frName: String? = null
    private var blName: String? = null
    private var brName: String? = null
    private var blDirection: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
    private var frDirection: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
    private var flDirection: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
    private var brDirection: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
    private var zeroPowerBehavior: DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    private var pathDeceleration: SlopeIntercept = SlopeIntercept(0, 0)
    fun setPathDeceleration(m: Double, b: Double): DriverW {
        pathDeceleration = SlopeIntercept(m, b)
        return this
    }

    fun setDrivetrainType(type: DRIVETYPE): DriverW {
        drivetrainType = type
        return this
    }

    fun setFrontLeftName(name: String): DriverW {
        flName = name
        return this
    }

    fun setFrontRightName(name: String): DriverW {
        frName = name
        return this
    }

    fun setBackLeftName(name: String): DriverW {
        blName = name
        return this
    }

    fun setBackRightName(name: String): DriverW {
        brName = name
        return this
    }

    fun reverseBackLeft(): DriverW {
        blDirection = DcMotorSimple.Direction.REVERSE
        return this
    }

    fun reverseBackRight(): DriverW {
        brDirection = DcMotorSimple.Direction.REVERSE
        return this
    }

    fun reverseFrontLeft(): DriverW {
        flDirection = DcMotorSimple.Direction.REVERSE
        return this
    }

    fun reverseFrontRight(): DriverW {
        frDirection = DcMotorSimple.Direction.REVERSE
        return this
    }

    fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior): DriverW {
        zeroPowerBehavior = behavior
        return this
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
    fun attachDrawer(drawer: Drawer): DriverW {
        this.drawer = drawer
        return this
    }

    private var opMode: LinearOpModeW? = null
    private var hwMap: HWMapW? = null

    internal lateinit var fl: DcMotorW
    internal lateinit var fr: DcMotorW
    internal lateinit var bl: DcMotorW
    internal lateinit var br: DcMotorW

    private val telemetry: TelemetryMaker = TelemetryMaker()

    fun build(opMode: LinearOpModeW) { // change to OpMode
        this.opMode = opMode
        this.hwMap = opMode.hardwareMap
        telemetry.attach(opMode.telemetry)
        verifyRequirements()
        initialize()
    }

    private fun verifyRequirements() { // TODO show more specific error messages
        val check1 = flName != null && frName != null && blName != null && brName != null
        val check2 =
            pathDeceleration != SlopeIntercept.zeros() && zeroPowerBehavior == DcMotor.ZeroPowerBehavior.BRAKE

        val final = check1 && check2
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
    }


    fun setWheelPower(powerCoefficients: DrivePowerCoefficients) {
        fl.power = powerCoefficients.frontLeft
        fr.power = powerCoefficients.frontRight
        bl.power = powerCoefficients.backLeft
        br.power = powerCoefficients.backRight
    }


    fun sendEncoders(): List<Pair<DcMotorW, String?>> {
        verifyRequirements()
        return listOf(Pair(fl, flName), Pair(fr, frName), Pair(bl, blName), Pair(br, brName))
    }


    internal fun findWheelVectors(data: MovementData): DrivePowerCoefficients {
        return findWheelVectors(data.y, data.x, data.rotation)
    }

    fun findWheelVectors(fwd: Double, strafe: Double, turn: Double): DrivePowerCoefficients {
        var frontLeft = 0.0
        var frontRight = 0.0
        var backLeft = 0.0
        var backRight = 0.0
        if (drivetrainType == DRIVETYPE.MECANUM) {
            frontLeft = fwd + strafe + turn
            frontRight = fwd - strafe - turn
            backLeft = fwd - strafe + turn
            backRight = fwd + strafe - turn
        } else { //TODO check this next
            frontLeft = fwd + turn
            frontRight = fwd - turn
            backLeft = fwd - turn
            backRight = fwd + turn
        }
        return DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
    }

    fun findWheelVectors(v: PathVector): DrivePowerCoefficients { // wrong this is [0..1]
        val fwd = v.translationalVector.a
        val strafe = v.translationalVector.b
        val turn = v.rotationalVector.toRadians()
        return findWheelVectors(fwd, strafe, turn)
    }

    fun addDriftCorrection(constraint: DrivePowerConstraint): DriverW {
        driftCoefficients = constraint
        return this
    }

    internal fun aconstructor(
        flObj: DcMotorW,
        frObj: DcMotorW,
        blObj: DcMotorW,
        brObj: DcMotorW
    ): DriverW {
        fl = flObj
        fr = frObj
        bl = blObj
        br = brObj
        return this
    }

    private var driftCoefficients: DrivePowerConstraint? = null
    fun setDriftCoefficients(coefficients: DrivePowerConstraint): DriverW {
        driftCoefficients = coefficients
        return this
    }

    fun applyDriftCorrection(coefficients: DrivePowerCoefficients): DrivePowerCoefficients {
        return if (driftCoefficients != null) {
            coefficients.applyConstraint(driftCoefficients!!)
        } else {
            coefficients
        }
    }
}