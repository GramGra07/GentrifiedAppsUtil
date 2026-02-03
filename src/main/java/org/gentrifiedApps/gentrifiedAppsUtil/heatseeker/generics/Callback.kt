package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions

open class Callback(
    open val callback: Runnable,
    val distance: Double? = null,
    val percent: Double? = null,
    val time: Double? = null
) {
    fun run() {
        callback.run()
    }

    fun atTime(time: Double): Boolean {
        return MathFunctions.inTolerance(time, this.time!!, 0.01)
    }

    fun atDistance(distance: Double): Boolean {
        return MathFunctions.inTolerance(distance, this.distance!!, 0.01)
    }
}