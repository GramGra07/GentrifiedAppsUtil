package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.feedback

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.hardware.sparkfun.SparkFunOTOS.Pose2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import kotlin.math.cos
import kotlin.math.sin

class Drawer {
    fun drawLocalization(target: Target2D) {
        val packet = TelemetryPacket()
        val fieldOverlay = packet.fieldOverlay()
        val roboRad = 8.0
        val color = "red"
        val h2 = target.h()
        val half2 = roboRad / 2
        val cos2 = cos(h2)
        val sin2 = sin(h2)
        val p1s2 = Pose2D(target.x + (sin2 * half2), target.y + (cos2 * half2), 0.0)
        val newS2 = Pose2D(target.x + (sin2 * roboRad), target.y + (cos2 * roboRad), 0.0)

        fieldOverlay
            .setStroke(color)
            .setFill(color)
            .strokeCircle(target.x, target.y, roboRad)
            .strokeLine(p1s2.x, p1s2.y, newS2.x, newS2.y)
    }
}