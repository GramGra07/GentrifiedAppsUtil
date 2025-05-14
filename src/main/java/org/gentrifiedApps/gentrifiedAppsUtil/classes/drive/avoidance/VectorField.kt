package org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.avoidance

import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.clip
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.distanceTo
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.AngleUnit
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.DirectionalVector
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

enum class AvoidanceVectorType {
    X, Y, BOTH, OFF
}

data class VectorField @JvmOverloads constructor(
    val point: Point,
    val radius: Double = 8.0,
    val avoidanceVectorType: AvoidanceVectorType = AvoidanceVectorType.BOTH
) {
    @JvmOverloads
    constructor(
        x: Double,
        y: Double,
        radius: Double = 8.0,
        avoidanceVectorType: AvoidanceVectorType = AvoidanceVectorType.BOTH
    ) : this(Point(x, y), radius, avoidanceVectorType)

    fun inField(point: Point): Boolean {
        return distanceTo(this.point, point) <= radius
    }

    fun fieldMovementVector(point: Point): DirectionalVector {
        val distance = distanceTo(this.point, point)
        val angle = MathFunctions.angleBetween(this.point, point)
        val magnitude = (radius - distance) / radius
        return DirectionalVector(clip(magnitude, -1.0, 1.0), Angle(angle, AngleUnit.RADIANS))
    }

    fun getXCorrection(point: Point): Double {
        return fieldMovementVector(point).xComponent()
    }

    fun getYCorrection(point: Point): Double {
        return fieldMovementVector(point).yComponent()
    }

    fun getCorrection(point: Point): DirectionalVector {
        return fieldMovementVector(point)
    }

    fun correctionAsDrive(point: Point): DrivePowerCoefficients {
        val correction = getCorrection(point)
        return when (avoidanceVectorType) {
            AvoidanceVectorType.X -> Driver.findWheelVectors(correction.xComponent(), 0.0, 0.0)
            AvoidanceVectorType.Y -> Driver.findWheelVectors(0.0, correction.yComponent(), 0.0)
            AvoidanceVectorType.BOTH -> Driver.findWheelVectors(
                correction.xComponent(),
                correction.yComponent(),
                0.0
            )

            AvoidanceVectorType.OFF -> DrivePowerCoefficients.zeros()
        }
    }
}

