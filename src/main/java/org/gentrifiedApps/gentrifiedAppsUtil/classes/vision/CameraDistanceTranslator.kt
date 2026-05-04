package org.gentrifiedApps.gentrifiedAppsUtil.classes.vision

import org.gentrifiedApps.gentrifiedAppsUtil.biobuzz.Pollen
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Vector
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Vector2d
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Distance

class CameraDistanceTranslator(val cameraLensIntrinsics: LensIntrinsics) {
    public fun estimateRelativePosition(
        pixelWidth: Double,
        center: Vector2d,
        realWorldWidth: Distance = Pollen.diameter
    ): Vector {
        val distanceZ = (cameraLensIntrinsics.fx * realWorldWidth.value) / pixelWidth
        val relativeX = ((center.a - cameraLensIntrinsics.cx) * distanceZ) / cameraLensIntrinsics.fx
        val relativeY = ((center.b - cameraLensIntrinsics.cy) * distanceZ) / cameraLensIntrinsics.fy
        return Vector(relativeX, relativeY, distanceZ)
    }
}