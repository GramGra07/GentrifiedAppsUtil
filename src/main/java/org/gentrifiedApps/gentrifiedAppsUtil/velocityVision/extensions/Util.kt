package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.extensions

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

object Util {
    fun createMask(hsv: Mat, lower: Scalar, upper: Scalar): Mat {
        val mask = Mat()
        Core.inRange(hsv, lower, upper, mask)
        Imgproc.erode(mask, mask, Mat(), Point(-1.0, -1.0), 2)
        Imgproc.dilate(mask, mask, Mat(), Point(-1.0, -1.0), 2)
        return mask
    }
    fun findContours(mask: Mat): List<MatOfPoint> {
        val contours: List<MatOfPoint> = ArrayList()
        val hierarchy = Mat()
        Imgproc.findContours(
            mask,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        return contours
    }
    fun getContourCenter(contour: MatOfPoint): Point? {
        val moments = Imgproc.moments(contour)
        if (moments._m00 != 0.0) {
            val cX = (moments._m10 / moments._m00).toInt()
            val cY = (moments._m01 / moments._m00).toInt()
            return Point(cX.toDouble(), cY.toDouble())
        }
        return null
    }
}