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
    var servo: ServoPlus?
) {
    var thetaTracker: TimeMachinePair<Double> = TimeMachinePair(0.0, 0.0)
    var alpha = 0.0
    fun findAlphaVal(theta: Double): Double {
        thetaTracker.current = theta
        alpha = ceil(slopeIntercept.getY(theta - offSetInitial))
        return alpha
    }

    private fun setServo(theta: Double, s: ServoPlus, offset: Double = 0.0) {
        thetaTracker.current = theta
        if (checkLast()) {
            if (servo == null) {
                servo = s
            }
            servo!!.position = findAlphaVal(theta) + offset
        }
        thetaTracker.previous = thetaTracker.current
    }

    fun setServoPosition(theta: Double, s: ServoPlus) {
        setServo(theta, s)
    }

    fun setServoPositionWithOffset(theta: Double, s: ServoPlus, offset: Double) {
        setServo(theta, s, offset)
    }

    fun checkLast(): Boolean {
        return thetaTracker.isDifferent()
    }
}