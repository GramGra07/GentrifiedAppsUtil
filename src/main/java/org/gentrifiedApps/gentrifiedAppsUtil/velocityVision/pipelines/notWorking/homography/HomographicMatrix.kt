package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.notWorking.homography

import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.CameraParams
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

internal class HomographicMatrix {
    fun warpToTopDown(input: Mat, cameraParams: CameraParams): Mat {
        // Extract intrinsics
        val fx = cameraParams.lensIntrinsics.fx!!
        val cx = cameraParams.lensIntrinsics.cx!!
        cameraParams.lensIntrinsics.fy!!
        cameraParams.lensIntrinsics.cy!!
        // Define real-world points (in inches) at the bottom of the camera view
        val fieldHeight =
            cameraParams.translationalVector.z  // Distance from the camera to the field in inches

        // Image points (from the tilted camera)
        val imgPoints = MatOfPoint2f(
            Point(0.0, input.rows().toDouble()),    // Bottom-left
            Point(input.cols().toDouble(), input.rows().toDouble()),  // Bottom-right
            Point(input.cols().toDouble(), 0.0),    // Top-right
            Point(0.0, 0.0)                         // Top-left
        )

        // Compute real-world coordinates using intrinsics
        val fieldPoints = MatOfPoint2f(
            Point(-cx * fieldHeight / fx, fieldHeight),  // Bottom-left
            Point((input.cols() - cx) * fieldHeight / fx, fieldHeight),  // Bottom-right
            Point((input.cols() - cx) * fieldHeight / fx, 0.0),  // Top-right
            Point(-cx * fieldHeight / fx, 0.0)   // Top-left
        )

        // Compute homography matrix
        val H = Imgproc.getPerspectiveTransform(imgPoints, fieldPoints)

        // Apply warp perspective
        val warped = Mat()
        Imgproc.warpPerspective(
            input,
            warped,
            H,
            Size(input.cols().toDouble(), input.rows().toDouble())
        )

        return warped
    }
}