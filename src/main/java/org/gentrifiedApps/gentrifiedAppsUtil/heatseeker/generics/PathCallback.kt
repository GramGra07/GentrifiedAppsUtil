package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D

class PathCallback(val callback: Runnable) :
    PathComponent(PathType.CALLBACK, Target2D.blank()) {
    fun run() {
        callback.run()
    }
}