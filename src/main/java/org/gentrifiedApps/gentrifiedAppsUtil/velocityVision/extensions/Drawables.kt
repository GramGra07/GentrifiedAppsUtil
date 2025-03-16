package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.extensions

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

object Drawables {
    fun cross(frame: Mat, center: Point, size: Int, color: Scalar) {
        Imgproc.line(frame, center, Point(center.x + size, center.y), color, 2)
        Imgproc.line(frame, center, Point(center.x - size, center.y), color, 2)
        Imgproc.line(frame, center, Point(center.x, center.y + size), color, 2)
        Imgproc.line(frame, center, Point(center.x, center.y - size), color, 2)
    }
}
