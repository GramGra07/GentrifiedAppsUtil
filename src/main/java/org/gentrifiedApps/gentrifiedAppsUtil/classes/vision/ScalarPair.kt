package org.gentrifiedApps.gentrifiedAppsUtil.classes.vision

import org.opencv.core.Scalar

class ScalarPair(var low: Scalar, var high: Scalar){
    constructor(low: Double, high: Double): this(Scalar(low), Scalar(high))
}