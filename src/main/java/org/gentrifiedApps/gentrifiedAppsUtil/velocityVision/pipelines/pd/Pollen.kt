package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.pd

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Vector
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Distance

class Pollen(var center: Vector) {
    companion object {
        val diameter: Distance = Distance(3.0)
    }

}