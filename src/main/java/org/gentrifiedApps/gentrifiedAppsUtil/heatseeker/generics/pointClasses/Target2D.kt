package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Point
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

data class Target2D(val x: Double, val y: Double, val angle: Angle) {
    constructor(x: Double, y: Double, angle: Double) : this(x, y, Angle(angle))
    fun distanceTo(target: Target2D): Double {
        return sqrt((target.x - x).pow(2) + (target.y - y).pow(2))
    }

    fun angleTo(target: Target2D): Double {
        return atan2(target.y - y, target.x - x)
    }

    fun h(): Double {
        return angle.toRadians()
    }

    fun toPose2D(): SparkFunOTOS.Pose2D {
        return SparkFunOTOS.Pose2D(x, y, angle.toRadians())
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
        fun zeros(): Target2D{
            return blank()
        }
    }
}
