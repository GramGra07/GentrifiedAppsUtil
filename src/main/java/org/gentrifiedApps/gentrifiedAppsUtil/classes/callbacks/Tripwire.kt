package org.gentrifiedApps.gentrifiedAppsUtil.classes.callbacks

class Tripwire(private val callback: () -> Boolean) {
    /**
     * Checks if the tripwire is tripped
     * @return true if the tripwire is tripped
     */
    fun isTripped(): Boolean {
        return callback()
    }

    /**
     * Checks if the tripwire is not tripped
     */
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