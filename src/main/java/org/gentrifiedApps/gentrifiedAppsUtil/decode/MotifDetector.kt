package org.gentrifiedApps.gentrifiedAppsUtil.decode

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

/**
 * Easy class to send back motif data, not to be used standalone as it will NOT do all of the work for you, you still must provide the AprilTagProcessor and VisionProcessor stuff, just call this method periodically to get the motif
 */
class MotifDetector(var aprilTag: AprilTagProcessor) {
    var pattern: PatternType = PatternType.NONE

    fun findMotif() {
        val currentDetections: MutableList<AprilTagDetection> = aprilTag.detections
        val tagsOfInterest = mapOf<Int, PatternType>(
            21 to PatternType.GPP,
            22 to PatternType.PGP,
            23 to PatternType.PPG
        )

        for (detection in currentDetections) {
            if (detection.id in tagsOfInterest.keys) {
                tagsOfInterest.getValue(detection.id).store()
                pattern = tagsOfInterest.getValue(detection.id)
                Scribe.instance.setSet("Motif")
                    .logData(tagsOfInterest.getValue(detection.id).toString())
                break
            }
        }
    }
}
