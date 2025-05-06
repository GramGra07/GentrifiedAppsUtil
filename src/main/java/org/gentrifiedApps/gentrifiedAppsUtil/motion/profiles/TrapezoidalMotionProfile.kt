package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles

/** Generates a trapezoidal velocity profile */
class TrapezoidalMotionProfile(private val maxVel: Double, private val maxAccel: Double) {
    private var startTime = 0.0
    private var totalTime = 0.0
    private var profile = listOf<Triple<Double, Double, Double>>() // (time, velocity, acceleration)
    fun regenerateProfile(distance: Double) {
        reset()
        generateProfile(distance)
    }

    fun reset() {
        startTime = 0.0
        totalTime = 0.0
        profile = listOf()
    }

    fun generateProfile(distance: Double): TrapezoidalMotionProfile {
        val accelTime = maxVel / maxAccel
        val accelDistance = 0.5 * maxAccel * accelTime * accelTime
        val minDistance = 2 * accelDistance

        require(distance >= minDistance) { "Distance is too small to reach max velocity and decelerate back to zero. Min is: $minDistance" }

        val cruiseDistance = distance - minDistance
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
        return this
    }

    fun start() {
        startTime = System.nanoTime() / 1e9
    }

    fun getTarget(): Pair<Double, Double> {
        if (startTime == 0.0) start()
        val currentTime = (System.nanoTime() / 1e9) - startTime
        val data = profile.find { it.first >= currentTime } ?: return Pair(0.0, 0.0)
        return Pair(data.second, data.third) // (velocity, acceleration)
    }

    fun getVelocity(): Double {
        return getTarget().first
    }
}
//val motionProfile = TrapezoidalMotionProfile(maxVel = 2.0, maxAccel = 1.0)
//
//    // Generate a profile for a distance of 10 units
//    motionProfile.generateProfile(distance = 10.0)
//
//    // Start the motion profile
//    motionProfile.start()
//
//    // Simulate a loop to get the target velocity and acceleration
//    while (true) {
//        val (velocity, acceleration) = motionProfile.getTarget()
//        println("Velocity: $velocity, Acceleration: $acceleration")
//
//        // Break the loop if the profile is complete
//        if (velocity == 0.0 && acceleration == 0.0) break
//
//        // Sleep for 20ms to simulate a control loop
//        Thread.sleep(20)
//    }