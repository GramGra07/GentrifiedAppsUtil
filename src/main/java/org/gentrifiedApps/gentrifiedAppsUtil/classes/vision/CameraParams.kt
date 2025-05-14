package org.gentrifiedApps.gentrifiedAppsUtil.classes.vision

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.RotationVector
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.TranslationalVector

data class CameraParams(
    val lensIntrinsics: LensIntrinsics,
    val rotationalVector: RotationVector,
    val translationalVector: TranslationalVector,
)
