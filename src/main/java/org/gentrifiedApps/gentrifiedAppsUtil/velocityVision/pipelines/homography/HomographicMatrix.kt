package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.homography

import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.CameraParams
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.cos
import kotlin.math.sin

class HomographicMatrix {
    companion object {
        @JvmStatic
        fun computeHomography(cameraParams: CameraParams): Mat {
            val fx = cameraParams.lensIntrinsics.fx
            val fy = cameraParams.lensIntrinsics.fy
            val cx = cameraParams.lensIntrinsics.cx
            val cy = cameraParams.lensIntrinsics.cy
            val yaw = cameraParams.rotationalVector.yaw
            val pitch = cameraParams.rotationalVector.pitch
            val roll = cameraParams.rotationalVector.roll
            // Intrinsics
            val K = Mat(3, 3, CvType.CV_64F)
            K.put(0, 0, fx, 0.0, cx)
            K.put(1, 0, 0.0, fy, cy)
            K.put(2, 0, 0.0, 0.0, 1.0)

            // Rotation from yaw/pitch/roll (ZYX)
            val cyaw = cos(yaw);
            val syaw = sin(yaw)
            val cpitch = cos(pitch);
            val spitch = sin(pitch)
            val croll = cos(roll);
            val sroll = sin(roll)

            val R = Mat(3, 3, CvType.CV_64F)
            R.put(
                0, 0,
                cyaw * cpitch,
                cyaw * spitch * sroll - syaw * croll,
                cyaw * spitch * croll + syaw * sroll,

                syaw * cpitch,
                syaw * spitch * sroll + cyaw * croll,
                syaw * spitch * croll - cyaw * sroll,

                -spitch,
                cpitch * sroll,
                cpitch * croll
            )

            // Add translation: camera height (Z axis)
            val cameraHeight = 12.0 // inches, tweak this!
            val T = Mat(3, 1, CvType.CV_64F)
            T.put(0, 0, 0.0)
            T.put(1, 0, 0.0)
            T.put(2, 0, cameraHeight)

            // Build 3x4 projection matrix [R|T]
            val RT = Mat(3, 4, CvType.CV_64F)
            for (i in 0..2) {
                for (j in 0..3) {
                    RT.put(i, j, if (j < 3) R.get(i, j)[0] else T.get(i, 0)[0])
                }
            }

            // Define ground plane Z = 0 → points on floor
            val H = Mat()
            val RT3x3 = RT.submat(0, 3, 0, 3) // Only use rotation part for planar homography

            val Kinv = Mat()
            Core.invert(K, Kinv)

            val temp = Mat()
            Core.gemm(RT3x3, Kinv, 1.0, Mat(), 0.0, temp)
            Core.gemm(K, temp, 1.0, Mat(), 0.0, H)

            return H
        }
    }
}