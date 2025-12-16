package org.gentrifiedApps.gentrifiedAppsUtil.classes.caching

import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController

class Sampler<T>(val ltc: LoopTimeController) {
    var l = mutableListOf<T>()
    var start: Int = 0

    init {
        start = ltc.totalLoops
    }

    fun getDif(): Int = ltc.totalLoops - start
    fun reset() {
        start = ltc.totalLoops
        l.clear()
    }

    fun getAvg(): T {
        return l.average() as T
    }

    fun List<T>.average() {
        var sum = 0
        this.forEach {
            sum += it as Int
        }
    }

    fun autoLoop(period: Int): T? {
        if (getDif() > period) {
            val t = getAvg()
            reset()
            return t
        }
        return null
    }
}
