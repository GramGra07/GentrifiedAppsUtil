package org.gentrifiedApps.gentrifiedAppsUtil.classes

class Tripwire(private val callback: () -> Boolean) {
    fun isTripped(): Boolean {
        return callback()
    }
    fun notIsTripped(): Boolean {
        return !callback()
    }
}