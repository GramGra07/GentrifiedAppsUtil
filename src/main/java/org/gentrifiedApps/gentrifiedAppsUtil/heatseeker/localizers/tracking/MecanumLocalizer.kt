package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.tracking

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.ComparablePair
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.TrackingLocalizer
import kotlin.math.cos
import kotlin.math.sin

class MecanumLocalizer(
    val driver: Driver, private val ticksPerIn: Double, private val trackWidth: Double,
    private val startPose: Target2D,
) : TrackingLocalizer() {
    private var pose: Target2D = startPose

    constructor(driver: Driver, ticksPerIn: Double, trackWidth: Double) : this(
        driver,
        ticksPerIn,
        trackWidth,
        Target2D(0.0, 0.0, Angle.blank())
    )

    var flComp = ComparablePair<Int>(0, 0)
    var frComp = ComparablePair<Int>(0, 0)
    var blComp = ComparablePair<Int>(0, 0)
    var brComp = ComparablePair<Int>(0, 0)

    private fun setLast() {
        flComp.second = flComp.first
        frComp.second = frComp.first
        blComp.second = blComp.first
        brComp.second = brComp.first
    }

    fun reset() {
        setPose(startPose)
        driver.resetDriveEncoders()
    }

    override fun initLocalizer() {
        driver.fl.reset()
        driver.fr.reset()
        driver.bl.reset()
        driver.br.reset()

        setLast()

        setPose(startPose)
    }

    override fun update() {
        flComp.first = driver.fl.currentPosition
        frComp.first = driver.fr.currentPosition
        blComp.first = driver.bl.currentPosition
        brComp.first = driver.br.currentPosition

        val deltaFl = (flComp.first - flComp.second) / ticksPerIn
        val deltaFr = (frComp.first - frComp.second) / ticksPerIn
        val deltaBl = (blComp.first - blComp.second) / ticksPerIn
        val deltaBr = (brComp.first - brComp.second) / ticksPerIn


        // calculate change in all positions
        val deltaFwd = ((deltaFl + deltaFr + deltaBl + deltaBr) / 4)
        val deltaRight = ((deltaFl - deltaFr - deltaBl + deltaBr) / 4)
        val deltaTurn = (deltaFl - deltaFr + deltaBl - deltaBr) / (4 * trackWidth)

        // calculate new position
        val deltaX = deltaRight * sin(pose.h()) - deltaFwd * cos(pose.h())
        val deltaY = deltaRight * cos(pose.h()) + deltaFwd * sin(pose.h())

        val xNew = pose.x + deltaX
        val yNew = pose.y + deltaY
        val hNew = pose.h() + deltaTurn

        setPose(Target2D(xNew, yNew, Angle(hNew).norm()))

        setLast()
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
        telemetry.addData(
            "FL Encoder", flComp.first
        )
        telemetry.addData(
            "FR Encoder", frComp.first
        )
        telemetry.addData(
            "BL Encoder", blComp.first
        )
        telemetry.addData(
            "BR Encoder", brComp.first
        )
        telemetry.update()
    }
}


private fun DcMotor.reset() {
    val prev = this.mode
    this.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    this.mode = prev
}
