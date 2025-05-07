package org.gentrifiedApps.gentrifiedAppsUtil.classes.callbacks

class Tripwire(private val callback: () -> Boolean) {
    fun isTripped(): Boolean {
        return callback()
    }

    fun notIsTripped(): Boolean {
        return !callback()
    }

    companion object {
        @JvmStatic
        fun newTripwire(callback: () -> Boolean): Tripwire {
            return Tripwire(callback)
        }
    }
}