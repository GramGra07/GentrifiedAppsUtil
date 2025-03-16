package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.tracking

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.Encoder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.EncoderSpecs
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.TrackingLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import kotlin.collections.get
import kotlin.math.PI
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

    private var fl: Encoder = Encoder(EncoderSpecs.ticksPerIn(ticksPerIn),driver.sendEncoders()[0].second, driver.sendEncoders()[0].first.direction,driver.hwMap)
    private var fr: Encoder = Encoder(EncoderSpecs.ticksPerIn(ticksPerIn),driver.sendEncoders()[1].second, driver.sendEncoders()[1].first.direction,true,driver.hwMap)
    private var bl: Encoder = Encoder(EncoderSpecs.ticksPerIn(ticksPerIn),driver.sendEncoders()[2].second, driver.sendEncoders()[2].first.direction,true,driver.hwMap)
    private var br: Encoder = Encoder(EncoderSpecs.ticksPerIn(ticksPerIn),driver.sendEncoders()[3].second, driver.sendEncoders()[3].first.direction,true,driver.hwMap)

    private fun setLast() {
        fl.setLastPosition()
        fr.setLastPosition()
        bl.setLastPosition()
        br.setLastPosition()
    }


    override fun initLocalizer() {
        fl.reset()
        fr.reset()
        bl.reset()
        br.reset()

        setLast()

        setPose(startPose)
    }

    override fun update() {
        val deltaFl = fl.getDeltaInches()
        val deltaFr = fr.getDeltaInches()
        val deltaBl = bl.getDeltaInches()
        val deltaBr = br.getDeltaInches()

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
        telemetry.addData("FL Encoder", fl.getTicks())
        telemetry.addData("FR Encoder", fr.getTicks())
        telemetry.addData("BL Encoder", bl.getTicks())
        telemetry.addData("BR Encoder", br.getTicks())
        telemetry.update()
    }
}