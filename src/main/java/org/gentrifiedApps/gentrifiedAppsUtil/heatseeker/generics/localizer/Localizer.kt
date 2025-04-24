package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D

abstract class Localizer {
    abstract fun initLocalizer()
    abstract fun update()
    abstract fun getPose(): Target2D
    abstract fun setPose(pose: Target2D)
    abstract fun getPoseError(pose: Target2D): Target2D
    abstract fun testEncoderDirection(telemetry: Telemetry)
}