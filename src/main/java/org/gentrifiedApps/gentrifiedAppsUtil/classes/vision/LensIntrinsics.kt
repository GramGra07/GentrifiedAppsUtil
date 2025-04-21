package org.gentrifiedApps.gentrifiedAppsUtil.classes.vision

import android.util.Log
import org.opencv.core.CvType
import org.opencv.core.Mat

interface LensIntrinsics {
    val fx: Double
    val fy: Double
    val cx: Double
    val cy: Double

    fun toMat(): Mat {
        val K = Mat(3, 3, CvType.CV_64F)
        K.put(0, 0, fx, 0.0, cx)
        K.put(1, 0, 0.0, fy, cy)
        K.put(2, 0, 0.0, 0.0, 1.0)
        try{return K}finally {
            K.release()
        }
    }
}

class LensIntrinsicsImpl @JvmOverloads constructor(
    override var fx: Double = 0.0,
    override var fy: Double = 0.0,
    override var cx: Double = 0.0,
    override var cy: Double = 0.0
) : LensIntrinsics
class LogitechC270LensIntrinsics : LensIntrinsics {
    override val fx: Double = 397.606
    override val fy: Double = 397.606
    override val cx: Double = 320.023
    override val cy: Double = 239.979
}