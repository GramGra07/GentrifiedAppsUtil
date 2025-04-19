package org.gentrifiedApps.gentrifiedAppsUtil.classes.vision

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.VisionHelpers
import org.opencv.core.Mat
import org.opencv.core.Scalar


class ScalarPair(val low: Scalar,val high: Scalar) : DualScalarPair(low, high, null, null){

    override fun applyThresh(input: Mat): Mat {
        return VisionHelpers.createMask(input,low,high)
    }
}