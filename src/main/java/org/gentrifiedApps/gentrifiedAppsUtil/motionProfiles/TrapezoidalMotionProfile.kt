package org.gentrifiedApps.gentrifiedAppsUtil.motionProfiles

/** Generates a trapezoidal velocity profile */
class TrapezoidalMotionProfile(private val maxVel: Double, private val maxAccel: Double) {
    private var startTime = 0.0
    private var totalTime = 0.0
    private var profile = listOf<Triple<Double, Double, Double>>() // (time, velocity, acceleration)

    fun generateProfile(distance: Double) {
        val accelTime = maxVel / maxAccel
        val accelDistance = 0.5 * maxAccel * accelTime * accelTime
        val cruiseDistance = distance - (2 * accelDistance)
        val cruiseTime = cruiseDistance / maxVel
        totalTime = (2 * accelTime) + cruiseTime

        val tempProfile = mutableListOf<Triple<Double, Double, Double>>()
        var time = 0.0
        val timeStep = 0.02 // 20ms per loop

        while (time <= totalTime) {
            val velocity = when {
                time < accelTime -> maxAccel * time
                time < accelTime + cruiseTime -> maxVel
                else -> maxVel - (maxAccel * (time - (accelTime + cruiseTime)))
            }
            val acceleration = when {
                time < accelTime -> maxAccel
                time < accelTime + cruiseTime -> 0.0
                else -> -maxAccel
            }
            tempProfile.add(Triple(time, velocity, acceleration))
            time += timeStep
        }
        profile = tempProfile
    }

    fun start() {
        startTime = System.nanoTime() / 1e9
    }

    fun getTarget(): Pair<Double, Double> {
        val currentTime = (System.nanoTime() / 1e9) - startTime
        val data = profile.find { it.first >= currentTime } ?: return Pair(0.0, 0.0)
        return Pair(data.second, data.third) // (velocity, acceleration)
    }
    fun getVelocity():Double{
        return getTarget().first
    }
}

/** PID + Feedforward controller */
class VelocityController(private val kP: Double, private val kI: Double, private val kD: Double, private val kF: Double, private val kA: Double) {
    private var prevError = 0.0
    private var integral = 0.0

    fun calculate(targetVel: Double, actualVel: Double, targetAccel: Double): Double {
        val error = targetVel - actualVel
        integral += error
        val derivative = error - prevError
        prevError = error

        // Feedforward (FF) + PID control
        return (kF * targetVel) + (kA * targetAccel) + (kP * error) + (kI * integral) + (kD * derivative)
    }
}