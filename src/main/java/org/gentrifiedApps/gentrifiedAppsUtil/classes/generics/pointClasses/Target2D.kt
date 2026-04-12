package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses

import com.acmerobotics.roadrunner.Pose2d
import com.pedropathing.geometry.Pose
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Vector2d
import org.gentrifiedApps.gentrifiedAppsUtil.dataStorage.DataStorage
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathVector
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

data class Target2D(val x: Double, val y: Double, val angle: Angle) {
    constructor(x: Double, y: Double, angle: Double) : this(x, y, Angle(angle))
    constructor(x: Double, y: Double) : this(x, y, Angle.blank())


    fun store() {
        DataStorage.setPose(this)
    }

    fun distanceTo(target: Target2D): Double {
        return sqrt((target.x - x).pow(2) + (target.y - y).pow(2))
    }

    fun angleTo(target: Target2D): Double {
        return atan2(target.y - y, target.x - x)
    }

    fun h(): Double {
        return angle.toRadians()
    }

    fun toPose(): Pose {
        return Pose(x, y, angle.toRadians())
    }


    fun toPose2d(): Pose2d {
        return Pose2d(x, y, angle.toRadians())
    }

    fun toPose2D2(): Pose2D {
        return Pose2D(
            DistanceUnit.INCH,
            x,
            y,
            AngleUnit.RADIANS,
            angle.toRadians()
        )
    }

    fun toPoint(): Point {
        return Point(x, y)
    }

    companion object {
        fun blank(): Target2D {
            return Target2D(0.0, 0.0, Angle.blank())
        }

        fun zeros(): Target2D {
            return blank()
        }


        fun fromPose(other: Pose): Target2D {
            return Target2D(other.x, other.y, other.heading)
        }

        fun fromPose2d(other: Pose2d): Target2D {
            return Target2D(
                other.position.x,
                other.position.y,
                Angle(
                    other.heading.toDouble(),
                    org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.AngleUnit.RADIANS
                )
            )
        }
    }

    operator fun minus(other: Target2D): Target2D {
        return Target2D(x - other.x, y - other.y, angle - other.angle)
    }

    operator fun times(other: Double): Target2D {
        return Target2D(x * other, y * other, angle)
    }

    operator fun div(other: Double): Target2D {
        return Target2D(x / other, y / other, angle)
    }

    fun vectorTo(other: Target2D): Vector2d {
        return Vector2d(other.x - x, other.y - y)
    }

    fun pVecTo(other: Target2D): PathVector {
        return PathVector(vectorTo(other), angle.norm() - other.angle.norm())
    }
}
