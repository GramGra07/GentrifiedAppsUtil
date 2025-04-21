package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.notWorking.poseSolver

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.CameraParams
import org.opencv.calib3d.Calib3d
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfDouble
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.MatOfPoint3f
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import kotlin.math.cos
import kotlin.math.sin

internal class MarkLocalizer {
    // Function to detect tape markers in the image
    fun detectTapeMarkers(frame: Mat, lowerColor: Scalar, upperColor: Scalar): List<Rect> {
        val hsvFrame = Mat()
        Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV)
        val mask = Mat()
        Core.inRange(hsvFrame, lowerColor, upperColor, mask)

        val contours = ArrayList<MatOfPoint>()
        Imgproc.findContours(
            mask,
            contours,
            Mat(),
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        val detectedMarkers = ArrayList<Rect>()
        for (contour in contours) {
            val approx = MatOfPoint2f()
            val epsilon = 0.02 * Imgproc.arcLength(MatOfPoint2f(*contour.toArray()), true)
            Imgproc.approxPolyDP(MatOfPoint2f(*contour.toArray()), approx, epsilon, true)

            if (approx.total() == 4L) {
                val rect = Imgproc.boundingRect(approx)
                detectedMarkers.add(rect)
            }
        }
        return detectedMarkers
    }

    // Function to calculate the 2D center of each detected marker
    fun calculateMarkerCenters(markers: List<Rect>): List<Point> {
        val centers = mutableListOf<Point>()
        for (marker in markers) {
            val centerX = marker.x + marker.width / 2
            val centerY = marker.y + marker.height / 2
            centers.add(Point(centerX.toDouble(), centerY.toDouble()))
        }
        return centers
    }

    // Function to convert rotation vector to yaw (heading angle)
    fun rotationToYaw(rvec: Mat): Double {
        val rotationMatrix = Mat()
        Calib3d.Rodrigues(rvec, rotationMatrix)
        return Math.atan2(rotationMatrix.get(1, 0)[0], rotationMatrix.get(0, 0)[0])
    }

    fun eulerAnglesToRotationMatrix(yaw: Double, pitch: Double, roll: Double): Mat {
        // Yaw rotation matrix (around Z axis)
        val R_yaw = Mat(3, 3, CvType.CV_64F)
        R_yaw.put(0, 0, cos(yaw), -sin(yaw), 0.0)
        R_yaw.put(1, 0, sin(yaw), cos(yaw), 0.0)
        R_yaw.put(2, 0, 0.0, 0.0, 1.0)

        // Pitch rotation matrix (around X axis)
        val R_pitch = Mat(3, 3, CvType.CV_64F)
        R_pitch.put(0, 0, 1.0, 0.0, 0.0)
        R_pitch.put(1, 0, 0.0, cos(pitch), -sin(pitch))
        R_pitch.put(2, 0, 0.0, sin(pitch), cos(pitch))

        // Roll rotation matrix (around Y axis)
        val R_roll = Mat(3, 3, CvType.CV_64F)
        R_roll.put(0, 0, cos(roll), 0.0, sin(roll))
        R_roll.put(1, 0, 0.0, 1.0, 0.0)
        R_roll.put(2, 0, -sin(roll), 0.0, cos(roll))

        // Combine the three rotations (Yaw -> Pitch -> Roll)
        val R = Mat(3, 3, CvType.CV_64F)
        Core.gemm(R_yaw, R_pitch, 1.0, Mat(), 0.0, R)
        val R_final = Mat(3, 3, CvType.CV_64F)
        Core.gemm(R, R_roll, 1.0, Mat(), 0.0, R_final)

        return R_final
    }

    // Function to estimate the robot's pose using solvePnP
    fun estimateRobotPose(
        cameraParams: CameraParams,
        objectPoints: MatOfPoint3f,
        imagePoints: MatOfPoint2f
    ): Pose2D {
        val distCoeffs = MatOfDouble(0.0, 0.0, 0.0, 0.0, 0.0)
        val rvec = Mat()
        val tvec = Mat()

        // SolvePnP to get the rotation and translation vectors
        Calib3d.solvePnP(objectPoints, imagePoints, cameraParams.lensIntrinsics.toMat(), distCoeffs, rvec, tvec)

        // Convert rotation vector to yaw (heading)
        val yaw = rotationToYaw(rvec)

        // Apply the camera pose (position and orientation of the camera on the robot) to adjust the estimated position
        // cameraPose represents the transformation from the camera to the robot frame

        // Camera Pose includes position (x, y, z) and orientation (yaw, pitch, roll)
        val cameraRotationMatrix = eulerAnglesToRotationMatrix(
            cameraParams.rotationalVector.yaw,
            cameraParams.rotationalVector.pitch,
            cameraParams.rotationalVector.roll
        )

        // Apply camera position to the translation vector (robot to camera position)
        val correctedPosition = Mat(3, 1, CvType.CV_64F)
        Core.gemm(cameraRotationMatrix, tvec, 1.0, Mat(), 0.0, correctedPosition)

        // Apply camera translation to the corrected position
        Core.add(
            correctedPosition,
            MatOfDouble(
                cameraParams.translationalVector.x,
                cameraParams.translationalVector.y,
                cameraParams.translationalVector.z
            ),
            correctedPosition
        )

        // Convert corrected position to 2D Pose
        val x = correctedPosition.get(0, 0)[0]
        val y = correctedPosition.get(1, 0)[0]

        return Pose2D(DistanceUnit.INCH, x, y, AngleUnit.DEGREES, yaw)
    }
}