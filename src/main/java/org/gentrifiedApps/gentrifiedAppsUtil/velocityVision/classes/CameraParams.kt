package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes

import org.opencv.core.CvType
import org.opencv.core.Mat

data class CameraParams(
    val lensIntrinsics: LensIntrinsics,
    val translationalVector: TranslationalVector,
    val rotationalVector: RotationVector
) {
    fun toMat(): Mat {
        val cameraMatrix = Mat(3, 3, CvType.CV_64F)
        cameraMatrix.put(0, 0, this.lensIntrinsics.fx!!)
        cameraMatrix.put(0, 2, this.lensIntrinsics.cx!!)
        cameraMatrix.put(1, 1, this.lensIntrinsics.fy!!)
        cameraMatrix.put(1, 2, this.lensIntrinsics.cy!!)
        cameraMatrix.put(2, 2, 1.0)
        return cameraMatrix
    }
}
