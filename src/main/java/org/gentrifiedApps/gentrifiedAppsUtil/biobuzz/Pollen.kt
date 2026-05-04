package org.gentrifiedApps.gentrifiedAppsUtil.biobuzz

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Vector2d
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Distance

class Pollen(var center: Vector2d) {
    companion object {
        val diameter: Distance = Distance(3.0)
    }

}