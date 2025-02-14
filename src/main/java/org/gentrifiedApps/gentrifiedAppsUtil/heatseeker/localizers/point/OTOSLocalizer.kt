package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.PointLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D

class OTOSLocalizer : PointLocalizer() {
    private var pose: Target2D = Target2D(0.0, 0.0, Angle.blank())
    override fun update() {
        TODO("Not yet implemented")
    }

    override fun getPose(): Target2D {
        return this.pose
    }

    override fun setPose(pose: Target2D) {
        this.pose = pose
    }

    override fun getPoseError(pose: Target2D): Target2D {
        TODO("Not yet implemented")
    }
}