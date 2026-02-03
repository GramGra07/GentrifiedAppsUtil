package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

data class Path(
    internal var callbacks: MutableList<Callback>,
    public var waypoints: MutableList<PathComponent>
) {
    fun isFinished(currentIndex: Int): Boolean {
        return currentIndex >= waypoints.size
    }
}