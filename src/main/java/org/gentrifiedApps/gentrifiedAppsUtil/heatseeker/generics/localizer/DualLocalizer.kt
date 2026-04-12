package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.others.PPLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer.others.RRLocalizer

class DualLocalizer(private var localizer: Any, val startPose: Target2D) {
    init {
        assert(localizer is PPLocalizer || localizer is RRLocalizer)
    }

    private fun asRRLocalizer(): RRLocalizer? {
        return if (isRRLocalizer()) localizer as RRLocalizer
        else null
    }

    private fun asPPLocalizer(): PPLocalizer? {
        return if (isPPLocalizer()) localizer as PPLocalizer
        else null
    }

    private fun isRRLocalizer(): Boolean {
        return localizer is RRLocalizer
    }

    private fun isPPLocalizer(): Boolean {
        return localizer is PPLocalizer
    }

    fun initLocalizer() {
        setPose(startPose)
        if (isPPLocalizer()) {
            try {
                asPPLocalizer()?.resetIMU()
            } catch (e: Exception) {

            }
        }
    }

    fun update() {
        if (isPPLocalizer()) {
            asPPLocalizer()?.update()
        } else if (isRRLocalizer()) {
            asRRLocalizer()?.update()
        }
    }

    fun getPose(): Target2D {
        return if (isPPLocalizer()) Target2D.fromPose(asPPLocalizer()!!.pose)
        else if (isRRLocalizer()) Target2D.fromPose2d(asRRLocalizer()!!.pose)
        else Target2D.blank()
    }

    fun setPose(pose: Target2D) {

        if (isPPLocalizer()) {
            asPPLocalizer()?.setStartPose(pose.toPose())
        } else if (isRRLocalizer()) {
            asRRLocalizer()?.pose = pose.toPose2d()
        }
    }

    fun getPoseError(pose: Target2D): Target2D {// is needed?
        return getPose().minus(pose)
    }
}