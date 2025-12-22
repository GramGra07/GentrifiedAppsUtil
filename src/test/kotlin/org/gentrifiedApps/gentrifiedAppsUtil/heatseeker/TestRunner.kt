package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.withinEpsilon
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.junit.jupiter.api.Test
import kotlin.math.cos
import kotlin.math.sin

class TestRunner() {
    @Test
    fun testTestRunner() {
        movements = listOf(DrivePowerCoefficients.of(1.0))
        assert(withinEpsilon(calculatePosition().x, 0.0))
        assert(withinEpsilon(calculatePosition().y, 1.0))
    }

    var movements: List<DrivePowerCoefficients> = emptyList()
    var idx = 0


    fun calculatePosition(): Target2D {
        var current = Target2D(0.0, 0.0, Angle.blank())
        for (move in movements) {
            val deltaFl = move.frontLeft
            val deltaFr = move.frontRight
            val deltaBl = move.backLeft
            val deltaBr = move.backRight

            // calculate change in all positions
            val deltaFwd = ((deltaFl + deltaFr + deltaBl + deltaBr) / 4)
            val deltaRight = ((deltaFl - deltaFr - deltaBl + deltaBr) / 4)
            val deltaTurn = (deltaFl - deltaFr + deltaBl - deltaBr) / (4 * 10)

            // calculate new position
            val deltaX = deltaRight * sin(current.h()) - deltaFwd * cos(current.h())
            val deltaY = deltaRight * cos(current.h()) + deltaFwd * sin(current.h())

            val xNew = current.x + deltaX
            val yNew = current.y + deltaY
            val hNew = current.h() + deltaTurn
            current = Target2D(xNew, yNew, Angle(hNew).norm())
        }
        return current
    }

    fun run(d: Driver) {
        while (idx < movements.size) {
            step(d)
        }
    }

    fun step(d: Driver) {
        require(idx < movements.size)
        val move = movements[idx]
        d.setWheelPower(move)
        idx++
    }
}