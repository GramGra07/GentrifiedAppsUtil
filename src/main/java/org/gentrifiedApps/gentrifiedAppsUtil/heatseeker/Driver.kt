package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.Drawer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback.TelemetryMaker
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.Localizer


enum class DRIVETYPE {
    MECANUM
}

class Driver(
    val opMode: LinearOpMode,
    flName: String,
    frName: String,
    blName: String,
    brName: String,
    private val flDirection: Direction,
    private val frDirection: Direction,
    private val blDirection: Direction,
    private val brDirection: Direction,
    var localizer: Localizer
) {
    val driveType = DRIVETYPE.MECANUM
    private val hwMap: HardwareMap = opMode.hardwareMap
    private var fl: DcMotor = hwMap.get(DcMotor::class.java, flName)
    private var fr: DcMotor = hwMap.get(DcMotor::class.java, frName)
    private var bl: DcMotor = hwMap.get(DcMotor::class.java, blName)
    private var br: DcMotor = hwMap.get(DcMotor::class.java, brName)

    val drawer = Drawer()
    private val telemetry: TelemetryMaker = TelemetryMaker()

    init {
        initialize()
    }

    private fun initialize() {
        fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        fr.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        bl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        br.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        fl.direction = flDirection
        fr.direction = frDirection
        bl.direction = blDirection
        br.direction = brDirection

        localizer.initLocalizer()
        drawer.drawLocalization(localizer.getPose())
        telemetry.sendTelemetry(opMode.telemetry, localizer.getPose())
    }

    fun update() {
        updatePoseEstimate()
        drawer.drawLocalization(localizer.getPose())
        telemetry.sendTelemetry(opMode.telemetry, localizer.getPose())
    }

    private fun updatePoseEstimate() {
        localizer.update()
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


    fun sendEncoders(): List<DcMotor> {
        return listOf(fl, fr, bl, br)
    }

    companion object {
        fun findWheelVectors(fwd: Double, strafe: Double, turn: Double): DrivePowerCoefficients {
            val frontLeft = fwd + strafe + turn
            val frontRight = fwd - strafe - turn
            val backLeft = fwd - strafe + turn
            val backRight = fwd + strafe - turn

            return DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
        }
    }
}