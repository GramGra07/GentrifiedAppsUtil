package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.homography

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import org.firstinspires.ftc.robotcore.external.function.Consumer
import org.firstinspires.ftc.robotcore.external.function.Continuation
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.CameraParams
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.homography.HomographicMatrix.Companion.fullHomography
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.util.concurrent.atomic.AtomicReference

private class HomographicProjection(
    val cameraParams: CameraParams,
) : VisionProcessor,
    CameraStreamSource {
    private val lastFrame = AtomicReference(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565))

    override fun init(width: Int, height: Int, calibration: CameraCalibration) {
    }

    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any {
        val output = fullHomography(frame, cameraParams)

        // Convert to bitmap for rendering
        val b = Bitmap.createBitmap(output.width(), output.height(), Bitmap.Config.ARGB_8888)

        Utils.matToBitmap(output, b)
        lastFrame.set(b)

        try {
            return output
        } finally {
            output.release()
        }
    }

    override fun onDrawFrame(
        canvas: Canvas,
        onscreenWidth: Int,
        onscreenHeight: Int,
        scaleBmpPxToCanvasPx: Float,
        scaleCanvasDensity: Float,
        userContext: Any
    ) {
        val bitmap = lastFrame.get()
        canvas.scale(scaleBmpPxToCanvasPx, scaleBmpPxToCanvasPx)
        canvas.drawRect(0F, 0F, onscreenWidth.toFloat(), onscreenHeight.toFloat(), Paint())
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    override fun getFrameBitmap(continuation: Continuation<out Consumer<Bitmap>?>) {
        continuation.dispatch { bitmapConsumer: Consumer<Bitmap>? ->
            bitmapConsumer!!.accept(
                lastFrame.get()
            )
        }
    }
}