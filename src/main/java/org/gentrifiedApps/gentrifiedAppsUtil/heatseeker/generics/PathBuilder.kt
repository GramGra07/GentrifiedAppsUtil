package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint

class PathBuilder {
    private val waypoints = mutableListOf<Waypoint>()

    fun addWaypoint(waypoint: Waypoint): PathBuilder {
        waypoints.add(waypoint)
        return this
    }

    fun build(): List<Waypoint> {
        return waypoints
    }
}