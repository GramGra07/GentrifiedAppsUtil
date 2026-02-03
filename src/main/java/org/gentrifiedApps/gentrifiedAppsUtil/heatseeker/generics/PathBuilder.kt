package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import kotlinx.coroutines.Runnable
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import kotlin.math.cos
import kotlin.math.sin

class PathBuilder {
    private val waypoints = mutableListOf<PathComponent>()
    private val callbacks = mutableListOf<Callback>()

    private fun lastWaypoint(): PathComponent {
        if (waypoints.isEmpty()) return PathComponent(Target2D.blank())
        return waypoints.last()
    }

    fun insertCallback(callback: Runnable): PathBuilder {
        waypoints.add(PathCallback(callback))
        return this
    }

    fun distanceCallback(distance: Double, callback: Runnable): PathBuilder {
        callbacks.add(Callback(callback, distance = distance))
        return this
    }

    //TODO add javadox
    fun percentCallback(percent: Double, callback: Runnable): PathBuilder {
        callbacks.add(Callback(callback, percent = percent))
        return this
    }

    fun timeCallback(time: Double, callback: Runnable): PathBuilder {
        callbacks.add(Callback(callback, time = time))
        return this
    }

    fun start(pathComponent: PathComponent): PathBuilder {
        waypoints.add(0, pathComponent)
        return this
    }

    fun addPath(pathComponent: PathComponent): PathBuilder {
        waypoints.add(pathComponent)
        return this
    }

    fun addPath(target: Target2D): PathBuilder {
        waypoints.add(PathComponent(target))
        return this
    }

    fun turnTo(heading: Angle, currentTarget: Target2D): PathBuilder {
        waypoints.add(PathComponent(heading, currentTarget))
        return this
    }

    fun moveTo(target: Target2D): PathBuilder {
        waypoints.add(PathComponent(target))
        return this
    }


    fun turn(heading: Angle): PathBuilder {
        val newHeading = lastWaypoint().target.h()
        waypoints.add(PathComponent(Angle(newHeading + heading.toRadians()), lastWaypoint().target))
        return this
    }

    fun forward(distance: Double): PathBuilder {
        val newX = lastWaypoint().target.x + distance * cos(lastWaypoint().target.h())
        val newY = lastWaypoint().target.y + distance * sin(lastWaypoint().target.h())
        return moveTo(Target2D(newX, newY, lastWaypoint().target.h()))
    }

    fun back(distance: Double): PathBuilder {
        val newX = lastWaypoint().target.x - distance * cos(lastWaypoint().target.h())
        val newY = lastWaypoint().target.y - distance * sin(lastWaypoint().target.h())
        return moveTo(Target2D(newX, newY, lastWaypoint().target.h()))
    }

    fun right(distance: Double): PathBuilder {
        val newX = lastWaypoint().target.x + distance * sin(lastWaypoint().target.h())
        val newY = lastWaypoint().target.y - distance * cos(lastWaypoint().target.h())
        return moveTo(Target2D(newX, newY, lastWaypoint().target.h()))
    }

    fun left(distance: Double): PathBuilder {
        val newX = lastWaypoint().target.x - distance * sin(lastWaypoint().target.h())
        val newY = lastWaypoint().target.y + distance * cos(lastWaypoint().target.h())
        return moveTo(Target2D(newX, newY, lastWaypoint().target.h()))
    }

    fun build(): Path {
        return Path(callbacks.build(), waypoints)
    }

    private fun MutableList<Callback>.build(): MutableList<Callback> {
        val callbacks: MutableList<Callback> = mutableListOf()
        val totalDistance = waypoints.totalDistance()
        forEach {
            if (it.distance != null) {
                // checks that distance not greater than total distance
                if (it.distance > totalDistance) {
                    throw Exception("Distance is greater than total distance for distance callback")
                }
                callbacks.add(it)
            } else if (it.percent != null) {
                val distance = it.percent * totalDistance
                callbacks.add(Callback(it.callback, distance = distance))
            } else if (it.time != null) {
                callbacks.add(it)
            }
        }
        return callbacks
    }
}

fun MutableList<PathComponent>.totalDistance(): Double {
    var totalDistance = 0.0
    forEach { totalDistance += it.target.distanceTo(last().target) }
    return totalDistance
}
