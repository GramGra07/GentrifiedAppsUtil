package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.path.Path

class PathBuilder {
    private val waypoints = mutableListOf<Path>()

    fun addWaypoint(waypoint: Waypoint): PathBuilder {
        waypoints.add(Path(waypoint))
        return this
    }

    fun turnTo(heading: Angle, velocity: Double, currentTarget: Target2D): PathBuilder {
        waypoints.add(Path(heading, velocity, currentTarget))
        return this
    }

    fun build(): List<Path> {
        return waypoints
    }
}