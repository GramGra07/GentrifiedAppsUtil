package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D

public enum class PathType {
    MOVE_TO,
    TURN_TO,
    CALLBACK
}

open class PathComponent
@JvmOverloads
constructor(
    val type: PathType,
    val target: Target2D,
    val velocity: Double = 1.0
) {

    @JvmOverloads
    constructor(target: Target2D, velocity: Double = 1.0) : this(PathType.MOVE_TO, target, velocity)

    constructor(heading: Angle, holdPosition: Target2D) : this(
        PathType.TURN_TO,
        Target2D(holdPosition.x, holdPosition.y, heading)
    )

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