package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses

import kotlin.math.cos
import kotlin.math.sin

data class DirectionalVector(val magnitude: Double, val direction: Angle) {
    fun xComponent(): Double {
        return magnitude * cos(direction.toRadians())
    }

    fun yComponent(): Double {
        return magnitude * sin(direction.toRadians())
    }
}
