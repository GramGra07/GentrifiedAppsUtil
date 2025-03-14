package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa

import android.graphics.Bitmap
import android.graphics.Canvas
import org.firstinspires.ftc.robotcore.external.function.Consumer
import org.firstinspires.ftc.robotcore.external.function.Continuation
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.enums.CSpace
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa.DetectionExtensions.first
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa.DetectionExtensions.second
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa.builderInterfaces.AssumedDetectionBuilder
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import java.util.concurrent.atomic.AtomicReference

class MeanColorOfAreaDetector(
    colorSpace: CSpace,
    builder: DetectionBuilder,
    builder2: DetectionBuilder,
    assumption: AssumedBuilder
) : VisionProcessor, CameraStreamSource {
    constructor(builder: DetectionBuilder, builder2: DetectionBuilder, assumption: AssumedBuilder) :
            this(CSpace.RGB, builder, builder2, assumption)


    private val lastFrame = AtomicReference(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565))
    private var colorSpaceMat = Mat()
    private var submatOne = Mat()
    private var submatTwo = Mat()
    private val builders: List<DetectionBuilder>
    private val assumption: AssumedBuilder
    private val colorSpace: CSpace

    init {
        this.builders = listOf(builder, builder2)
        this.assumption = assumption
        this.colorSpace = colorSpace
    }

    override fun init(width: Int, height: Int, calibration: CameraCalibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565))
    }

    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any {
        val firstBuilder: DetectionBuilder = builders.first()
        val secondBuilder: DetectionBuilder = builders.second()
        val assumedBuilder: AssumedDetectionBuilder = assumption
        builders.forEach { builder ->
            Imgproc.rectangle(
                frame, builder.rectangle.br(), builder.rectangle.tl(), Scalar(0.0, 255.0, 0.0), 1
            )
        }
        when (colorSpace) {
            CSpace.RGB -> null
            CSpace.YCrCb -> Imgproc.cvtColor(frame, colorSpaceMat, Imgproc.COLOR_RGB2YCrCb)
            CSpace.HSV -> Imgproc.cvtColor(frame, colorSpaceMat, Imgproc.COLOR_RGB2HSV)
            CSpace.HLS -> Imgproc.cvtColor(frame, colorSpaceMat, Imgproc.COLOR_RGB2HLS)
        }
        submatOne = colorSpaceMat.submat(
            firstBuilder.rectangle
        )
        submatTwo = colorSpaceMat.submat(
            secondBuilder.rectangle
        )
        colorSpaceMat.release()
        val oneMean = Core.mean(submatOne).`val`
        val twoMean = Core.mean(submatTwo).`val`
        submatOne.release()
        submatTwo.release()
        if (executeIfInRange(firstBuilder.scalarLow, firstBuilder.scalarHigh, oneMean)) {
            drawRectangleAndText(firstBuilder, frame)
            firstBuilder.execute()
        } else if (executeIfInRange(secondBuilder.scalarLow, secondBuilder.scalarHigh, twoMean)) {
            drawRectangleAndText(secondBuilder, frame)
            secondBuilder.execute()
        } else {
            Imgproc.putText(
                frame,
                assumedBuilder.name,
                Point((frame.width() / 2).toDouble(), (frame.height() / 2).toDouble()),
                0,
                5.0,
                Scalar(0.0, 255.0, 0.0)
            )
            assumedBuilder.execute()
        }
        val b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565)
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

    private fun executeIfInRange(
        scalarLow: Scalar,
        scalarHigh: Scalar,
        mean: DoubleArray
    ): Boolean {
        return (mean[0] > scalarLow.`val`[0] && mean[0] < scalarHigh.`val`[0]) &&
                (mean[1] > scalarLow.`val`[1] && mean[1] < scalarHigh.`val`[1]) &&
                (mean[2] > scalarLow.`val`[2] && mean[2] < scalarHigh.`val`[2])
    }

    private fun drawRectangleAndText(builder: DetectionBuilder, frame: Mat) {
        Imgproc.rectangle(
            frame, builder.rectangle.br(), builder.rectangle.tl(), Scalar(0.0, 255.0, 0.0), 1
        )
        Imgproc.putText(
            frame,
            builder.name,
            Point((frame.width() / 2).toDouble(), (frame.height() / 2).toDouble()),
            0,
            5.0,
            Scalar(0.0, 255.0, 0.0)
        )
    }
}
