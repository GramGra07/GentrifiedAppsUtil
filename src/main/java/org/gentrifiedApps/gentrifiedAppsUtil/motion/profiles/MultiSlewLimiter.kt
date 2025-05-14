package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles

class MultiSlewLimiter {
    var limiters: MutableMap<Pair<String, Double>, SlewRateLimiter> = mutableMapOf()
    fun addLimiter(name: String, maxRate: Double): MultiSlewLimiter {
        limiters[name to maxRate] = SlewRateLimiter(maxRate)
        return this
    }

    internal fun length(): Int {
        return limiters.size
    }

    fun calculate(name: String, input: Double): Double {
        val limiter =
            limiters[name to limiters.keys.find { it.first == name }?.second]
        return limiter?.calculate(
            input
        ) ?: input
    }
}