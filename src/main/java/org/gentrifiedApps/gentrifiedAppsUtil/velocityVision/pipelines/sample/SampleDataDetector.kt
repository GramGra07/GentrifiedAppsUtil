package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.sample

import android.graphics.Bitmap
import android.graphics.Canvas
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.function.Consumer
import org.firstinspires.ftc.robotcore.external.function.Continuation
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.Color
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.sample.CameraLock.Companion.ofReturnables
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs

class SampleDataDetector(
    private val returnType: List<ReturnType>,
    alliance: Alliance,
) : VisionProcessor,
    CameraStreamSource {
    constructor(alliance: Alliance) : this(ReturnType.all(), alliance)
    constructor() : this(Alliance.BLUE)
    constructor(returnType: ReturnType) : this(listOf(returnType), Alliance.BLUE)

    private var isBlue: Boolean = (alliance == Alliance.BLUE)
    private var cameraLock: CameraLock = CameraLock.empty()
    private val lastFrame = AtomicReference(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565))

    private fun createMask(hsv: Mat, lower: Scalar, upper: Scalar): Mat {
        val mask = Mat()
        Core.inRange(hsv, lower, upper, mask)
        Imgproc.erode(mask, mask, Mat(), Point(-1.0, -1.0), 2)
        Imgproc.dilate(mask, mask, Mat(), Point(-1.0, -1.0), 2)
        return mask
    }

    private fun findContours(mask: Mat): List<MatOfPoint> {
        val contours: List<MatOfPoint> = ArrayList()
        val hierarchy = Mat()
        Imgproc.findContours(
            mask,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        return contours
    }

    private fun getContourCenter(contour: MatOfPoint): Point? {
        val moments = Imgproc.moments(contour)
        if (moments._m00 != 0.0) {
            val cX = (moments._m10 / moments._m00).toInt()
            val cY = (moments._m01 / moments._m00).toInt()
            return Point(cX.toDouble(), cY.toDouble())
        }
        return null
    }

    override fun init(width: Int, height: Int, calibration: CameraCalibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565))
    }

    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any {
        val blurred = Mat()
        Imgproc.GaussianBlur(frame, blurred, Size(15.0, 15.0), 0.0)

        val hsv = Mat()
        Imgproc.cvtColor(blurred, hsv, Imgproc.COLOR_RGB2HSV)

        val lowerBlue = Scalar(100.0, 150.0, 0.0)
        val upperBlue = Scalar(140.0, 255.0, 255.0)
        val lowerRed1 = Scalar(0.0, 150.0, 0.0)
        val upperRed1 = Scalar(10.0, 255.0, 255.0)
        val lowerRed2 = Scalar(170.0, 150.0, 0.0)
        val upperRed2 = Scalar(180.0, 255.0, 255.0)
        val lowerYellow = Scalar(25.0, 100.0, 100.0)
        val upperYellow = Scalar(70.0, 255.0, 255.0)

        val yellowMask = createMask(hsv, lowerYellow, upperYellow)

        var colorMask: Mat
        val colorName = if (isBlue) {
            colorMask = createMask(hsv, lowerBlue, upperBlue)
            "blue"
        } else {
            val mask1 = createMask(hsv, lowerRed1, upperRed1)
            val mask2 = createMask(hsv, lowerRed2, upperRed2)
            Core.bitwise_or(mask1, mask2, Mat().also { colorMask = it })
            "red"
        }

        val yellowContours = findContours(yellowMask)
        val colorContours = findContours(colorMask)

        val allContours: MutableList<MatOfPoint> = ArrayList(yellowContours)
        allContours.addAll(colorContours)

        if (allContours.isEmpty()) {
            cameraLock = CameraLock.empty()
            return arrayOf(MatOfPoint(), frame, intArrayOf(0, 0, 0, 0, 0))
        }

        val height: Int = frame.rows()
        val width: Int = frame.cols()
        val center = Point((width / 2).toDouble(), (height / 2).toDouble())

        val closestContour = allContours.stream()
            .min { c1: MatOfPoint?, c2: MatOfPoint? ->
                val p1 = getContourCenter(c1!!)
                val p2 = getContourCenter(c2!!)
                if (p1 == null) return@min 1
                if (p2 == null) return@min -1
                val dist1 = abs(p1.x - center.x) + abs(p1.y - center.y)
                val dist2 = abs(p2.x - center.x) + abs(p2.y - center.y)
                dist1.compareTo(dist2)
            }
            .orElse(MatOfPoint())

        val rect = Imgproc.minAreaRect(MatOfPoint2f(*closestContour.toArray()))
        val boxPoints = arrayOfNulls<Point>(4)
        rect.points(boxPoints)

        Imgproc.drawContours(
            frame,
            listOf(MatOfPoint(*boxPoints)),
            0,
            Scalar(0.0, 255.0, 0.0),
            2
        )

        val contourCenter = getContourCenter(closestContour)
        val cx = contourCenter!!.x.toInt()
        val cy = contourCenter.y.toInt()

        var angle = rect.angle
        angle = if (rect.size.width < rect.size.height) {
            abs(angle)
        } else {
            90 - abs(angle)
        }

        val wrappedAngle = (-(56 / 90.0) * angle + 63).toInt()

        // Determine color of the selected contour
        val mask = Mat.zeros(frame.size(), CvType.CV_8U)
        Imgproc.drawContours(mask, listOf(closestContour), 0, Scalar(255.0), -1)

        val yellowAndMask = Mat()
        val colorAndMask = Mat()

        Core.bitwise_and(mask, yellowMask, yellowAndMask)
        Core.bitwise_and(mask, colorMask, colorAndMask)

        val selectedColor =
            if (Core.countNonZero(yellowAndMask) > Core.countNonZero(colorAndMask)) {
                "yellow"
            } else {
                colorName
            }

        Imgproc.putText(
            frame, "Angle: $wrappedAngle°", Point(10.0, 30.0),
            Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, Scalar(0.0, 255.0, 0.0), 2
        )

        Imgproc.putText(
            frame, "CX: $cx, CY: $cy", Point(10.0, 90.0),
            Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, Scalar(0.0, 255.0, 0.0), 2
        )

        cameraLock = CameraLock(angle.toDouble(), selectedColor.toColor(), contourCenter)
        cameraLock.draw(frame)

        val b =
            Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565)
        Utils.matToBitmap(frame, b)
        lastFrame.set(b)

        yellowAndMask.release()
        colorAndMask.release()
        mask.release()
        hsv.release()
        blurred.release()
        yellowMask.release()
        colorMask.release()

        return frame
    }

    fun String.toColor(): Color {
        return when (this) {
            "yellow" -> Color.YELLOW
            "red" -> Color.RED
            "blue" -> Color.BLUE
            else -> Color.NONE
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
    }

    override fun getFrameBitmap(continuation: Continuation<out Consumer<Bitmap>?>) {
        continuation.dispatch { bitmapConsumer: Consumer<Bitmap>? ->
            bitmapConsumer!!.accept(
                lastFrame.get()
            )
        }
    }

    fun telemetry(telemetry: Telemetry) {
        telemetry.addData("Camera Lock", cameraLock.toString())
    }

    fun returnCameraLock(): CameraLock {
        return cameraLock.ofReturnables(returnType)
    }
}