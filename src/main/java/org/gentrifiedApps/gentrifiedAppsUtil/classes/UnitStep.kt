package org.gentrifiedApps.gentrifiedAppsUtil.classes

class UnitStep(val a: Double) {
    var startSeconds = System.currentTimeMillis() / 1000
    fun reset() {
        startSeconds = System.currentTimeMillis() / 1000
    }

    var switch: Boolean = false

    fun update(): Boolean {
        if (switch) return true
        val now = System.currentTimeMillis() / 1000
        val dif = now - startSeconds
        if (dif >= a && !switch) {
            switch = true
        }
        return switch
    }
}