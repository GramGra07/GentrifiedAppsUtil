package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles

import com.qualcomm.robotcore.util.ElapsedTime

class PWMMotionProfile {

    enum class PWMType {
        LOOPS, SECONDS
    }

    private var setPower = 0.0
    private var loops = 0.0
    private var timer: ElapsedTime = ElapsedTime()
    private var maxPower: Double
    private var type: PWMType
    private var period: Double
    private fun isTimerBased(): Boolean {
        return type == PWMType.SECONDS
    }

    private fun alternatePower() {
        setPower = if (setPower == 0.0) {
            maxPower
        } else {
            0.0
        }
    }

    @JvmOverloads
    constructor(type: PWMType, period: Double, maxPower: Double = 1.0) {
        this.maxPower = maxPower
        this.type = type
        this.period = period
    }

    init {
        if (isTimerBased()) timer.reset()
    }

    fun update(): Double {
        if (isTimerBased()) {
            if (timer.seconds() >= period) {
                alternatePower()
                timer.reset()
            }
        } else {
            if (loops >= period) {
                alternatePower()
                loops = 0.0
            }
            loops++
        }
        return setPower
    }

    fun getPower(): Double {
        return setPower
    }
}