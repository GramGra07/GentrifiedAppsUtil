package org.gentrifiedApps.gentrifiedAppsUtil.classes.vision

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.RotationVector
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.TranslationalVector
import org.opencv.core.CvType
import org.opencv.core.Mat

data class CameraParams(
    val lensIntrinsics: LensIntrinsics,
    val translationalVector: TranslationalVector,
    val rotationalVector: RotationVector
) {
}
