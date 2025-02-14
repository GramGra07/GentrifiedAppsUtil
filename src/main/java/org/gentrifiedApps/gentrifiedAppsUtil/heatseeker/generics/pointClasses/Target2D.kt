package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses

import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

data class Target2D(val x: Double, val y: Double, val angle: Angle) {
    fun distanceTo(target: Target2D): Double {
        return sqrt((target.x - x).pow(2) + (target.y - y).pow(2))
    }

    fun angleTo(target: Target2D): Double {
        return atan2(target.y - y, target.x - x)
    }

    companion object {
        fun blank(): Target2D {
            return Target2D(0.0, 0.0, Angle.blank())
        }
    }
}
