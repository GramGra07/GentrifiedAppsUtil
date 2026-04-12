package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
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
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.DualLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DcMotorW


//    val loc = DualLocalizer(TwoDeadWheelLocalizer(hwMap, imu, 29, Pose2d.blank()), Target2D.blank())
//    val loc2 = DualLocalizer(TwoWheelLocalizer(hwMap, TwoWheelConstants()), Target2D.blank())


class Driver {
    enum class DRIVETYPE {
        MECANUM,
        TANK
    }

    private var drivetrainType: Driver.DRIVETYPE = Driver.DRIVETYPE.MECANUM
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

    fun setDrivetrainType(type: Driver.DRIVETYPE): Driver {
        drivetrainType = type
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

    fun setLocalizer(localizer: DualLocalizer): Driver {
        this.localizer = localizer
        this.localizer!!.initLocalizer()
        localizerTelemetry()
        return this
    }


    public var localizer: DualLocalizer? = null

    fun hasLocalizer(): Boolean {
        return localizer != null
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

    var drawer = Drawer()

    var opMode: LinearOpMode? = null
    private var hwMap: HardwareMap? = null

    internal lateinit var fl: DcMotor
    internal lateinit var fr: DcMotor
    internal lateinit var bl: DcMotor
    internal lateinit var br: DcMotor

    private lateinit var telemetry: TelemetryMaker


    fun build(opMode: LinearOpMode) {
        this.opMode = opMode
        this.hwMap = opMode.hardwareMap
        telemetry = TelemetryMaker(opMode.telemetry)
        verifyRequirements()
        initialize()
    }

    private fun verifyRequirements() {
        val check1 = flName != null && frName != null && blName != null && brName != null

        val final = check1
        if (!final && opMode != null) {
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

        localizer?.initLocalizer()
        localizerTelemetry()
        pose = localizer?.getPose()
        lastPose = pose
    }

    fun localizerTelemetry() {
        if (localizer != null) {
            drawer.drawLocalization(localizer!!.getPose())
            telemetry.showPose(localizer!!.getPose())
        }
    }

    internal fun update() {
        if (localizer != null) {
            localizer!!.update()
            getCurrentPose()
            localizerTelemetry()
        }
    }

    fun getCurrentPose(): Target2D {
        lastPose = pose
        pose = localizer!!.getPose()
        return pose!!
    }

    var pose: Target2D? = null
    var lastPose: Target2D? = null

    fun getPoseDiff(): Double {
        return lastPose!!.distanceTo(pose!!)
    }

    fun setWheelPower(powerCoefficients: DrivePowerCoefficients) {
        fl.power = powerCoefficients.frontLeft
        fr.power = powerCoefficients.frontRight
        bl.power = powerCoefficients.backLeft
        br.power = powerCoefficients.backRight
    }


    fun sendEncoders(): List<Pair<DcMotor, String?>> {
        verifyRequirements()
        return listOf(Pair(fl, flName), Pair(fr, frName), Pair(bl, blName), Pair(br, brName))
    }

    fun findWheelVectors(fwd: Double, strafe: Double, turn: Double): DrivePowerCoefficients {
        var frontLeft = 0.0
        var frontRight = 0.0
        var backLeft = 0.0
        var backRight = 0.0
        if (drivetrainType == Driver.DRIVETYPE.MECANUM) {
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


    fun findWheelVectors(v: PathVector): DrivePowerCoefficients {
        val fwd = v.translationalVector.a
        val strafe = v.translationalVector.b
        val turn = v.rotationalVector.angle
        return findWheelVectors(fwd, strafe, turn).normalizeTo1()
    }

    fun aconstructor(flObj: DcMotor, frObj: DcMotor, blObj: DcMotor, brObj: DcMotor): Driver {
        fl = flObj
        fr = frObj
        bl = blObj
        br = brObj
        return this
    }

    var driftCoefficients: DrivePowerConstraint? = null
    fun setDriftCoefficients(coefficients: DrivePowerConstraint): Driver {
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

    internal companion object {
        fun findWheelVecs(v: Vector): DrivePowerCoefficients {
            val fwd = v.b
            val strafe = v.a
            val turn = v.c
            return Driver.Companion.findWheelVectors(fwd, strafe, turn)
        }

        fun findWheelVectors(fwd: Double, turn: Double): DrivePowerCoefficients {
            // tank drive
            val strafe = 0.0
            return findWheelVectors(fwd, strafe, turn)
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