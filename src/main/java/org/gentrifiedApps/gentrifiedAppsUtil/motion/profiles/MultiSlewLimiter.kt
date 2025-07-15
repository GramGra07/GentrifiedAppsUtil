package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles

enum class Slews {
    SlewLimiter,
    OnlyUpSlew
}

class MultiSlewLimiter(val slew: Slews) {
    var limiters: MutableMap<Pair<String, Double>, SlewRateLimiter> = mutableMapOf()
    var onlyUpSlewRateLimiters: MutableMap<Pair<String, Double>, OnlyUpSlewRateLimiter> =
        mutableMapOf()

    fun addOnlyUpSlewLimiter(name: String, maxRate: Double): MultiSlewLimiter {
        require(slew == Slews.OnlyUpSlew)
        onlyUpSlewRateLimiters[name to maxRate] = OnlyUpSlewRateLimiter(maxRate)
        return this
    }

    fun addLimiter(name: String, maxRate: Double): MultiSlewLimiter {
        require(slew == Slews.SlewLimiter)
        limiters[name to maxRate] = SlewRateLimiter(maxRate)
        return this
    }

    internal fun length(): Int {
        return when (slew) {
            Slews.SlewLimiter -> limiters.size
            Slews.OnlyUpSlew -> onlyUpSlewRateLimiters.size
        }
    }

    fun calculate(name: String, input: Double): Double {
        return when (slew) {
            Slews.SlewLimiter -> {
                val limiter =
                    limiters[name to limiters.keys.find { it.first == name }?.second]
                limiter?.calculate(
                    input
                ) ?: input
            }

            Slews.OnlyUpSlew -> {
                val limiter =
                    onlyUpSlewRateLimiters[name to onlyUpSlewRateLimiters.keys.find { it.first == name }?.second]
                limiter?.calculate(
                    input
                ) ?: input
            }
        }
    }
}