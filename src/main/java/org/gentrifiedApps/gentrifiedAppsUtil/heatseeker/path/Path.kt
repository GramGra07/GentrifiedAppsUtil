package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.path

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Waypoint

enum class PathType {
    MOVE_TO,
    TURN_TO,
}

data class Path(val type: PathType, val target: Target2D, val velocity: Double) {
    constructor (waypoint: Waypoint) : this(PathType.MOVE_TO, waypoint.target2D, waypoint.velocity)
    constructor(target: Target2D, velocity: Double) : this(PathType.MOVE_TO, target, velocity)
    constructor(heading: Angle, velocity: Double, currentTarget: Target2D) : this(
        PathType.TURN_TO,
        Target2D(currentTarget.x, currentTarget.y, heading),
        velocity
    )

    constructor(heading: Angle, velocity: Double, x: Double, y: Double) : this(
        PathType.TURN_TO,
        Target2D(x, y, heading),
        velocity
    )

    fun waypoint(): Waypoint {
        return Waypoint(target.x, target.y, target.angle, velocity)
    }

    fun x(): Double {
        return target.x
    }

    fun y(): Double {
        return target.y
    }

    fun h(): Double {
        return target.h()
    }
}