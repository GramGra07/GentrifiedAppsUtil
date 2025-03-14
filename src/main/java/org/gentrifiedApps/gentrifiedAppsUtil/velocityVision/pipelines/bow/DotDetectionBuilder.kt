package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow.interfaces.DotDetectionInterface
import org.opencv.core.Rect

class DotDetectionBuilder(
    override val rectangle: Rect,
    override val minArea: Double,
    override val maxArea: Double
) : DotDetectionInterface{
    constructor() : this(Rect(), 0.0, 1000000.0)
}