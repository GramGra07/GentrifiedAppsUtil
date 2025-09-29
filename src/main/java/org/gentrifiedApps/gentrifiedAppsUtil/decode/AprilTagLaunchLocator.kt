package org.gentrifiedApps.gentrifiedAppsUtil.decode

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle

data class AprilTagPhysicsData(
    var range: Double,
    var x: Double,
    var robotOrientation: Double
)

class AprilTagLaunchLocator(var aprilTag: AprilTagProcessor, val height: Double) {
    val tagsOfInterest = listOf<Int>(20, 24)
    val projectileMotionCalculator = ProjectileMotionCalculator()
    var aprilTagPhysicsData = AprilTagPhysicsData(0.0, 0.0, 0.0)

    fun detect(): AprilTagPhysicsData? {
        val detections = aprilTag.detections
        for (detection in detections) {
            if (detection.id in tagsOfInterest) {
                val r = getRange(detection)
                val x = getX(detection)
                val theta = getRobotOrientation(detection)
                aprilTagPhysicsData = AprilTagPhysicsData(r, x, theta)
                return aprilTagPhysicsData
            }
        }
        return null
    }

    fun getRange(detection: AprilTagDetection): Double {
        return detection.ftcPose.y
    }

    fun getX(detection: AprilTagDetection): Double {
        return detection.ftcPose.x //-___________+
    }

    fun getRobotOrientation(detection: AprilTagDetection): Double {
        return detection.ftcPose.bearing //-__________+ turn right to increase
    }

    fun getWillHitTarget(initialVelocity: Double, launchAngle: Angle): Boolean {
        return projectileMotionCalculator.willLand(
            initialVelocity,
            launchAngle,
            height,
            aprilTagPhysicsData.range
        )
    }

    fun getRequiredLaunchAngles(initialVelocity: Double): List<Angle> {
        return projectileMotionCalculator.calculateRequiredLaunchAngles(
            initialVelocity,
            aprilTagPhysicsData.range
        )
    }

    fun getRequiredInitialVelocity(launchAngle: Angle): Double {
        return projectileMotionCalculator.calculateRequiredInitialVelocity(
            launchAngle,
            aprilTagPhysicsData.range
        )
    }


}