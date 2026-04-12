package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

data class Path(
    var callbacks: MutableList<Callback>,
    var waypoints: MutableList<PathComponent>
) {
    fun isFinished(currentIndex: Int): Boolean {
        return currentIndex >= waypoints.size
    }
}