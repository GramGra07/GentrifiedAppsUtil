package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.notWorking.poseSolver

import android.graphics.Bitmap
import android.graphics.Canvas
import org.firstinspires.ftc.robotcore.external.function.Consumer
import org.firstinspires.ftc.robotcore.external.function.Continuation
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.CameraParams
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.KnownMark
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX
import java.util.concurrent.atomic.AtomicReference

private class PoseSolver(
    val cameraParams: CameraParams,
    val knownMark: KnownMark,
) : VisionProcessor,
    CameraStreamSource {

    private val lastFrame = AtomicReference(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565))

    override fun init(width: Int, height: Int, calibration: CameraCalibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565))
    }

    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any {
        val lowerColor = Scalar(0.0, 0.0, 100.0)  // Lower bound for red color in HSV
        val upperColor = Scalar(100.0, 100.0, 255.0)  // Upper bound for red color in HSV

        // Detect tape markers
        val markers = MarkLocalizer().detectTapeMarkers(frame, lowerColor, upperColor)

        // Calculate the 2D centers of the detected markers
        val markerCenters = MarkLocalizer().calculateMarkerCenters(markers)

        val imagePoints = MatOfPoint2f(*markerCenters.toTypedArray())
        val objectPoints = knownMark.toMatOfPoint3f()
        val robotPose = MarkLocalizer().estimateRobotPose(cameraParams, objectPoints, imagePoints)
        Imgproc.putText(
            frame,
            "Robot pose: $robotPose",
            Point(10.0, 50.0),
            FONT_HERSHEY_SIMPLEX,
            1.0,
            Scalar(255.0, 255.0, 255.0),
            2
        )

        val b =
            Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565)
        Utils.matToBitmap(frame, b)
        lastFrame.set(b)
        return frame
    }

    override fun onDrawFrame(
        canvas: Canvas,
        onscreenWidth: Int,
        onscreenHeight: Int,
        scaleBmpPxToCanvasPx: Float,
        scaleCanvasDensity: Float,
        userContext: Any
    ) {
    }

    override fun getFrameBitmap(continuation: Continuation<out Consumer<Bitmap>?>) {
        continuation.dispatch { bitmapConsumer: Consumer<Bitmap>? ->
            bitmapConsumer!!.accept(
                lastFrame.get()
            )
        }
    }
}