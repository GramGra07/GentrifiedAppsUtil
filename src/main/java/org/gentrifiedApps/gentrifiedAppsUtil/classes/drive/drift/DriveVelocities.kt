package org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Quadruple
import kotlin.math.abs

data class DriveVelocities(
    var frontLeft: Double,
    var frontRight: Double,
    var backLeft: Double,
    var backRight: Double
){
    constructor(fl: Int, fr: Int, bl: Int, br: Int) : this(fl.toDouble(), fr.toDouble(), bl.toDouble(), br.toDouble())
    operator fun div(b: Double): DriveVelocities {
        return DriveVelocities(
            this.frontLeft / b,
            this.frontRight / b,
            this.backLeft / b,
            this.backRight / b
        )
    }
    fun min() : Double {
        return minOf(frontLeft, frontRight, backLeft, backRight)
    }
    fun max() : Double {
        return maxOf(frontLeft, frontRight, backLeft, backRight)
    }
    fun asPercent() : DriveVelocities {
        return this / max()
    }

    internal fun applyDriftNormalizer(): Quadruple<Double>{
        return applyDriftNormalizer(0.1)
    }
    internal fun applyDriftNormalizer(tolerance: Double): Quadruple<Double>{
        val min = min()
        val max = max()
        val dif = abs(max-min)
        if (dif >= tolerance) {
            val frontLeft = 1+((dif * (((1 - frontLeft) / dif) - 1)))
            val frontRight = 1+((dif * (((1 - frontRight) / dif) - 1)))
            val backLeft = 1+((dif * (((1 - backLeft) / dif) - 1)))
            val backRight = 1+((dif * (((1 - backRight) / dif) - 1)))
            return Quadruple(frontLeft, frontRight, backLeft, backRight)
        }
        return Quadruple(0.0)
    }
}
