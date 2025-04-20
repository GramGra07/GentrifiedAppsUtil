package org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

class DriftTunerOpMode : LinearOpMode() {
    val driver = Driver(this, "fl", "fr", "bl", "br")
    val timer = ElapsedTime()

    override fun runOpMode() {
        waitForStart()
        timer.reset()
        if (opModeIsActive()) {
            while (timer.seconds() < 5 && opModeIsActive() && !isStopRequested) {
                driver.setWheelPower(DrivePowerCoefficients(1.0))
            }
            driver.setWheelPower(DrivePowerCoefficients(0.0))
            val velocitiesP = driver.getPositions().asPercent()
            val driftV = velocitiesP.applyDriftNormalizer()
            while (opModeIsActive() && !isStopRequested) {
                if (velocities.all0()) {
                    telemetry.addData("Drift Tuner", "No drift detected")
                } else {
                    telemetry.addData("Drift Tuner", "Drift detected")
                    telemetry.addData("Front Left", velocitiesP.first)
                    telemetry.addData("Front Right", velocitiesP.second)
                    telemetry.addData("Back Left", velocitiesP.third)
                    telemetry.addData("Back Right", velocitiesP.fourth)
                    telemetry.addLine(
                    "$round(driftV.first,2),$round(driftV.second,2),$round(driftV.third,2),$round(driftV.fourth,2)")
                }
                telemetry.update()
            }

        }
    }

}