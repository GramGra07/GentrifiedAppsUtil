package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.poseSolver

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.opencv.calib3d.Calib3d
import org.opencv.core.Mat
import org.opencv.core.MatOfDouble
import org.opencv.core.MatOfPoint2f
import org.opencv.core.MatOfPoint3f

class ObjectRelocalizer {

    fun findRobotPose(
        cameraMatrix: Mat,
        objectPoints: MatOfPoint3f,
        imagePoints: MatOfPoint2f
    ): Pose2D {
        // Distortion coefficients (assuming no distortion)
        val distCoeffs = MatOfDouble(0.0, 0.0, 0.0, 0.0, 0.0)

        // Matrices for rotation and translation vectors
        val rvec = Mat()
        val tvec = Mat()

        // Solve PnP to get rotation and translation vectors
        Calib3d.solvePnP(objectPoints, imagePoints, cameraMatrix, distCoeffs, rvec, tvec)

        // Convert rotation vector to yaw (heading angle)
        val yaw = rotationToYaw(rvec)

        // Get position (X, Y) from translation vector
        val x = tvec.get(0, 0)[0] // X position
        val y = tvec.get(1, 0)[0] // Y position

        // Return Pose2D (robot position and heading)
        return Pose2D(DistanceUnit.INCH, x, y, AngleUnit.DEGREES, yaw)
    }

    fun rotationToYaw(rvec: Mat): Double {
        // Convert the rotation vector to a rotation matrix
        val rotationMatrix = Mat()
        Calib3d.Rodrigues(rvec, rotationMatrix)

        // Extract the yaw angle (rotation around the Z-axis)
        return Math.atan2(rotationMatrix.get(1, 0)[0], rotationMatrix.get(0, 0)[0])
    }

    fun rotationToEulerAngles(rvec: Mat): Triple<Double, Double, Double> {
        // Convert the rotation vector to a rotation matrix
        val rotationMatrix = Mat()
        Calib3d.Rodrigues(rvec, rotationMatrix)

        // Convert the rotation matrix to Euler angles
        val pitch = Math.atan2(rotationMatrix.get(2, 1)[0], rotationMatrix.get(2, 2)[0])
        val roll = Math.atan2(
            rotationMatrix.get(2, 0)[0],
            Math.sqrt(
                Math.pow(rotationMatrix.get(2, 1)[0], 2.0) + Math.pow(
                    rotationMatrix.get(
                        2,
                        2
                    )[0], 2.0
                )
            )
        )
        val yaw = Math.atan2(rotationMatrix.get(1, 0)[0], rotationMatrix.get(0, 0)[0])

        return Triple(pitch, roll, yaw)
    }


}