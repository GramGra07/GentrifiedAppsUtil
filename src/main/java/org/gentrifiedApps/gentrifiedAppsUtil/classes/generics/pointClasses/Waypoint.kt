package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses

data class Waypoint @JvmOverloads constructor(val target2D: Target2D, val velocity: Double = 1.0) {
    @JvmOverloads
    constructor(x: Double, y: Double, angle: Angle, velocity: Double = 1.0) : this(
        Target2D(x, y, angle), velocity
    )

    init {
        require(velocity >= 0) { "Velocity must be greater than or equal to 0" }
        require(velocity <= 1) { "Velocity must be less than 1" }
    }

    val x = target2D.x
    val y = target2D.y
    val h = target2D.h()
}
