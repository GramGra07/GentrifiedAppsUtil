package org.gentrifiedApps.gentrifiedAppsUtil.classes.vision

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.VisionHelpers
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Scalar

open class DualScalarPair constructor(var lower1: Scalar, var upper1: Scalar, var lower2: Scalar?, var upper2: Scalar?) {
    constructor(lower1: Scalar, upper1: Scalar) : this(lower1, upper1, null, null)

    constructor(lower1: Double, upper1: Double, lower2: Double, upper2: Double) : this(
        Scalar(lower1, lower1, lower1, lower1),
        Scalar(upper1, upper1, upper1, upper1),
        Scalar(lower2, lower2, lower2, lower2),
        Scalar(upper2, upper2, upper2, upper2)
    )

    fun toScalarPair(): ScalarPair {
        return ScalarPair(lower1, upper1)
    }
    fun isPair(): Boolean {
        return lower2 == null && upper2 == null
    }

    /**
     * Applies the threshold to the input image and returns a mask.
     * Make sure that the cSpace is the correct type when applying the threshold.
     */
    open fun applyThresh(input: Mat): Mat{
        val mask1 = VisionHelpers.createMask(input, lower1, upper1)
        if (isPair()) {
            return mask1
        }

        val mask2 = VisionHelpers.createMask(input, lower2!!, upper2!!)
        val combinedMask = Mat()
        Core.bitwise_or(mask1, mask2, combinedMask)

        // Release temporary masks to prevent memory leaks
        mask1.release()
        mask2.release()

        return combinedMask
    }
}