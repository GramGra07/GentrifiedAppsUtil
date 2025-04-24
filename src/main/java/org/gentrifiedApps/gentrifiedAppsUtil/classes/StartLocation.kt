package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.dataStorage.DataStorage
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D

data class StartLocation @JvmOverloads constructor(
    var alliance: Alliance = DataStorage.getAlliance(),
    val startPose: Target2D = DataStorage.getPose(),
) {
    init {
        build()
    }

    fun build() {
        DataStorage.setData(alliance, startPose)
    }
}