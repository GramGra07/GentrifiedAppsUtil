package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Distance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.CameraDistanceTranslator
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.LensIntrinsicsImpl
import org.junit.jupiter.api.Test

class LensIntrinsicsTest {
    val lensIntrinsics = LensIntrinsicsImpl(820.0, 820.0, 320.0, 240.0)
    val estimator = CameraDistanceTranslator(lensIntrinsics)

    @Test
    fun testEstimator() {
        val estimation = estimator.estimateRelativePosition(
            100.0,
            Vector2d(400.0, 300.0),
            Distance(15.0)
        )
        println(estimation.toString())
        assert(
            estimation == Vector(12.0, 9.0, 123.0)
        )
    }
}