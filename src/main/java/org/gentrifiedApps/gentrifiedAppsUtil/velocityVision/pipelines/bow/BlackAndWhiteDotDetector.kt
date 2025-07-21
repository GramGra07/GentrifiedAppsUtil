package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.createBitmap
import org.firstinspires.ftc.robotcore.external.function.Consumer
import org.firstinspires.ftc.robotcore.external.function.Continuation
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.pow

class BlackAndWhiteDotDetector(
    dotColor: DotColor,
    builder: DotDetectionBuilder
) : VisionProcessor, CameraStreamSource {
    constructor() : this(DotColor.WHITE, DotDetectionBuilder())
    constructor(dotColor: DotColor) : this(dotColor, DotDetectionBuilder())
    constructor(builder: DotDetectionBuilder) : this(DotColor.WHITE, builder)

    private val lastFrame = AtomicReference(createBitmap(1, 1, Bitmap.Config.RGB_565))
    private var submat = Mat()
    private var dotColor: DotColor
    private var builder: DotDetectionBuilder
    private var gray = Mat()
    private var thresh = Mat()
    private var contours: List<MatOfPoint> = ArrayList()
    private var circles: Mat = Mat()
    private var masked: Mat = Mat()
    private var blur: Mat = Mat()
    private var hierarchy: Mat = Mat()
    private val dots: MutableList<Any> = ArrayList()

    init {
        this.dotColor = dotColor
        this.builder = builder
    }

    override fun init(width: Int, height: Int, calibration: CameraCalibration) {
        lastFrame.set(createBitmap(width, height, Bitmap.Config.RGB_565))
    }

    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any {
        val minArea = builder.minArea
        val maxArea = builder.maxArea
        submat = frame.submat(builder.rectangle)

        //!white
        when (dotColor) {
            DotColor.WHITE -> {
                Imgproc.medianBlur(submat, blur, 5)
                Imgproc.cvtColor(blur, gray, Imgproc.COLOR_BGR2GRAY)
                Imgproc.threshold(gray, thresh, 200.0, 255.0, Imgproc.THRESH_BINARY)
                Imgproc.findContours(
                    thresh,
                    contours,
                    hierarchy,
                    Imgproc.RETR_EXTERNAL,
                    Imgproc.CHAIN_APPROX_SIMPLE
                )
                for (c in contours) {
                    val area: Double = Imgproc.contourArea(c as Mat)
                    if (area > minArea && area < maxArea) {
                        Imgproc.drawContours(frame, contours, -1, Scalar(0.0, 255.0, 0.0), 2)
                        dots.add(c)
                    }
                }
            }

            DotColor.BLACK -> {
                Imgproc.cvtColor(submat, gray, Imgproc.COLOR_BGR2GRAY)
                Imgproc.medianBlur(gray, gray, 5)
                Imgproc.HoughCircles(
                    gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    0.1,
                    100.0, 30.0, 1, 100000000
                )
                val mask = Mat(submat.rows(), submat.cols(), CvType.CV_8U, Scalar.all(0.0))
                if (circles.cols() > 0) {
                    for (x in 0 until circles.cols()) {
                        val c: DoubleArray = circles.get(0, x)
                        val center = Point(
                            Math.round(c[0]).toDouble(), Math.round(
                                c[1]
                            ).toDouble()
                        )
                        val radius = Math.round(c[2]).toInt()
                        val area = Math.PI * c[2].pow(2.0)
                        if (area >= minArea && radius <= maxArea) {
                            Imgproc.circle(frame, center, radius, Scalar(0.0, 255.0, 0.0), -1, 8, 0)
                            dots.add(c)
                        }
                    }
                }
                frame.copyTo(masked, mask)
                Imgproc.threshold(mask, thresh, 1.0, 255.0, Imgproc.THRESH_BINARY)
                mask.release()
            }
        }
        Imgproc.putText(
            frame,
            getDotCount().toString(),
            Point((frame.width() / 16).toDouble(), (frame.height() / 6).toDouble()),
            Imgproc.FONT_HERSHEY_SIMPLEX,
            1.0,
            Scalar(0.0, 255.0, 0.0),
            2
        )
        submat.release()
        thresh.release()
        masked.release()
        gray.release()
        circles.release()
        blur.release()
        hierarchy.release()


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

    fun getDotCount(): Int {
        return dots.count()
    }
}