package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses

data class Waypoint(val target2D: Target2D, val velocity: Double) {
    constructor(x: Double, y: Double, h: Double, velocity: Double) : this(
        Target2D(
            x,
            y,
            Angle(h, AngleUnit.RADIANS)
        ), velocity
    )

    constructor(x: Double, y: Double, h: Angle, velocity: Double) : this(
        Target2D(x, y, h),
        velocity
    )

    init {
        require(velocity >= 0) { "Velocity must be greater than or equal to 0" }
        require(velocity <= 1) { "Velocity must be less than 1" }
    }

    val x = target2D.x
    val y = target2D.y
    val h = target2D.h()
}
