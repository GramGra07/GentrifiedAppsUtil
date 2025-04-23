package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D

abstract class TrackingLocalizer : Localizer() {
    abstract override fun update()
    abstract override fun getPose(): Target2D
    abstract override fun setPose(pose: Target2D)
    abstract override fun getPoseError(pose: Target2D): Target2D
    abstract override fun testEncoderDirection(telemetry: Telemetry)
}