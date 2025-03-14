package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa.builderInterfaces.ObjectDetectionBuilder
import org.opencv.core.Rect
import org.opencv.core.Scalar

/**
 * This interface is used to build a detection object
 * @property rectangle the rectangle of the detection
 * @property name the name of the detection
 * @property function the function to execute when the detection is detected
 */
class DetectionBuilder(
    override val rectangle: Rect,
    override val name: String,
    override val scalarLow: Scalar,
    override val scalarHigh: Scalar,
    private val function: Runnable?,
) : ObjectDetectionBuilder {
    constructor(
        rectangle: Rect,
        name: String,
        scalarLow: Scalar,
        scalarHigh: Scalar,
    ) : this(rectangle, name, scalarLow, scalarHigh, null)
    override fun execute() {
        function?.run()
    }
}