package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.archive.localizers

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.Localizer

abstract class PointLocalizer : Localizer() {
    abstract override fun initLocalizer()
    abstract override fun update()
    abstract override fun getPose(): Target2D
    abstract override fun setPose(pose: Target2D)
    abstract override fun getPoseError(pose: Target2D): Target2D
}