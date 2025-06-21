package org.gentrifiedApps.gentrifiedAppsUtil.controllers.kinematics

import org.gentrifiedApps.gentrifiedAppsUtil.classes.equations.SlopeIntercept
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.TimeMachinePair
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.servo.ServoPlus
import kotlin.math.ceil

class AutoLevel1DF @JvmOverloads constructor(
    var slope: Double,
    var offSetInitial: Double = 0.0,
    var slopeIntercept: SlopeIntercept = SlopeIntercept.autoLevel1DF(
        slope
    ),
) {
    private var thetaTracker: TimeMachinePair<Double> = TimeMachinePair(0.0, 0.0)
    private var alpha = 0.0
    fun findAlphaVal(theta: Double): Double {
        thetaTracker.current = theta
        alpha = ceil(slopeIntercept.getY(theta - offSetInitial))
        return alpha
    }

    private fun setServo(theta: Double, s: ServoPlus, offset: Double = 0.0): Double {
        thetaTracker.current = theta
        if (checkLast()) {
            s.position = findAlphaVal(theta) + offset
        }
        thetaTracker.previous = thetaTracker.current
        return s.position
    }

    fun setServoPosition(theta: Double, s: ServoPlus): Double {
        return setServo(theta, s)
    }

    fun setServoPositionWithOffset(theta: Double, s: ServoPlus, offset: Double): Double {
        return setServo(theta, s, offset)
    }

    private fun checkLast(): Boolean {
        return thetaTracker.isDifferent()
    }
}