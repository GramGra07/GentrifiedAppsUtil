package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.tracking

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.TrackingLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import kotlin.math.cos
import kotlin.math.sin

class MecanumLocalizer(
    driver: Driver, private val ticksPerIn: Double, private val trackWidth: Double,
    private val startPose: Target2D,
) : TrackingLocalizer() {
    private var pose: Target2D = Target2D(0.0, 0.0, Angle.blank())

    constructor(driver: Driver, ticksPerIn: Double, trackWidth: Double) : this(
        driver,
        ticksPerIn,
        trackWidth,
        Target2D(0.0, 0.0, Angle.blank())
    )

    private var fl: DcMotor = driver.sendEncoders()[0]
    private var fr: DcMotor = driver.sendEncoders()[1]
    private var bl: DcMotor = driver.sendEncoders()[2]
    private var br: DcMotor = driver.sendEncoders()[3]

    private var lastFl = 0
    private var lastFr = 0
    private var lastBl = 0
    private var lastBr = 0

    private fun reset() {
        lastFl = fl.currentPosition
        lastFr = fr.currentPosition
        lastBl = bl.currentPosition
        lastBr = br.currentPosition
    }


    override fun initLocalizer() {
        fl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        fr.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        br.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        reset()

        fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        fr.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        bl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        br.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        setPose(startPose)
    }

    override fun update() {
        val deltaFl = (fl.currentPosition - lastFl) / ticksPerIn
        val deltaFr = (fr.currentPosition - lastFr) / ticksPerIn
        val deltaBl = (bl.currentPosition - lastBl) / ticksPerIn
        val deltaBr = (br.currentPosition - lastBr) / ticksPerIn

        // calculate change in all positions
        val deltaFwd = ((deltaFl + deltaFr + deltaBl + deltaBr) / 4)
        val deltaRight = ((deltaFl - deltaFr - deltaBl + deltaBr) / 4)
        val deltaTurn = (deltaFl - deltaFr + deltaBl - deltaBr) / (4 * trackWidth)

        // calculate new position
        val deltaX = deltaRight * cos(pose.h()) - deltaFwd * sin(pose.h())
        val deltaY = deltaRight * sin(pose.h()) + deltaFwd * cos(pose.h())

        val xNew = pose.x + deltaX
        val yNew = pose.y + deltaY
        val hNew = pose.h() + deltaTurn

        setPose(Target2D(xNew, yNew, Angle(hNew)))

        lastFl = fl.currentPosition
        lastFr = fr.currentPosition
        lastBl = bl.currentPosition
        lastBr = br.currentPosition
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
        telemetry.addData("FL Encoder", fl.currentPosition)
        telemetry.addData("FR Encoder", fr.currentPosition)
        telemetry.addData("BL Encoder", bl.currentPosition)
        telemetry.addData("BR Encoder", br.currentPosition)
        telemetry.update()
    }
}