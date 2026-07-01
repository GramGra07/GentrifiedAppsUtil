package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.localizer

import com.acmerobotics.roadrunner.Pose2d
import com.pedropathing.geometry.Pose
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
        return if (isPPLocalizer()) {
            val pose = asPPLocalizer()!!.pose
            Target2D(pose.x, pose.y, pose.heading)
        } else if (isRRLocalizer()) {
            val pose = asRRLocalizer()!!.pose
            Target2D(pose.position.x, pose.position.y, pose.heading.toDouble())
        } else Target2D.blank()
    }

    fun setPose(pose: Target2D) {

        if (isPPLocalizer()) {
            val posePP = Pose(pose.x, pose.y, pose.h())
            asPPLocalizer()?.setStartPose(posePP)
        } else if (isRRLocalizer()) {
            val poseRR = Pose2d(pose.x, pose.y, pose.h())
            asRRLocalizer()?.pose = poseRR
        }
    }

    fun getPoseError(pose: Target2D): Target2D {// is needed?
        return getPose().minus(pose)
    }
}