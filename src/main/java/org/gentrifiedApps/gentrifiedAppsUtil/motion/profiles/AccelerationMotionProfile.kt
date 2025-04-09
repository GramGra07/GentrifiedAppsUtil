package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles

/** Generates an acceleration-only velocity profile */
class AccelerationMotionProfile(private val maxVel: Double, private val maxAccel: Double) {
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

    fun generateProfile(distance: Double) {
        val accelTime = Math.sqrt(2 * distance / maxAccel)
        totalTime = accelTime

        val tempProfile = mutableListOf<Triple<Double, Double, Double>>()
        var time = 0.0
        val timeStep = 0.02 // 20ms per loop

        while (time <= totalTime) {
            val velocity = maxAccel * time
            val acceleration = maxAccel
            tempProfile.add(Triple(time, velocity, acceleration))
            time += timeStep
        }

        // Add constant velocity phase
        while (time <= totalTime + 1.0) { // 1 second of constant velocity
            val velocity = maxVel
            val acceleration = 0.0
            tempProfile.add(Triple(time, velocity, acceleration))
            time += timeStep
        }

        profile = tempProfile
    }

    fun start() {
        startTime = System.nanoTime() / 1e9
    }

    fun getTarget(): Pair<Double, Double> {
        if (startTime == 0.0) start()
        val currentTime = (System.nanoTime() / 1e9) - startTime
        val data = profile.find { it.first >= currentTime } ?: return Pair(maxVel, 0.0)
        return Pair(data.second, data.third) // (velocity, acceleration)
    }

    fun getVelocity(): Double {
        return getTarget().first
    }
}