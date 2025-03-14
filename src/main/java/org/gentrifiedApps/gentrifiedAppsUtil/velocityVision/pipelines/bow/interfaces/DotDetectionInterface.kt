package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow.interfaces

import org.opencv.core.Rect

interface DotDetectionInterface {
    val rectangle: Rect
    val minArea: Double
    val maxArea: Double
}