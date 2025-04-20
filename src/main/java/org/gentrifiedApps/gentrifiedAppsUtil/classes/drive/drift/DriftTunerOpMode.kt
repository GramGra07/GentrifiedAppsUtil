package org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.round
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

class DriftTunerOpMode @JvmOverloads constructor(val time: Int = 5 ) : LinearOpMode() {

    override fun runOpMode() {
        Scribe.instance.startLogger(this)
        val driver = Driver(this, "fl", "fr", "bl", "br")
        driver.setupOpMode(this)
        val timer = ElapsedTime()
        timer.reset()
        waitForStart()
        if (opModeIsActive()) {
            while (opModeIsActive() && !isStopRequested) {
                Scribe.instance.setSet("Drift Tuner").logDebug("${timer.seconds()}, $time")
                if (timer.seconds() >= time) {
                    break
                }
                driver.setWheelPower(DrivePowerCoefficients(1.0))
            }
            driver.setWheelPower(DrivePowerCoefficients(0.0))
            val velocitiesP = driver.getPositions().asPercent()
            val driftV = velocitiesP.applyDriftNormalizer()
            while (opModeIsActive() && !isStopRequested) {
                if (driftV.all0()) {
                    telemetry.addData("Drift Tuner", "No drift detected")
                } else {
                    telemetry.addData("Drift Tuner", "Drift detected")
                    telemetry.addData("Front Left", velocitiesP.frontLeft)
                    telemetry.addData("Front Right", velocitiesP.frontRight)
                    telemetry.addData("Back Left", velocitiesP.backLeft)
                    telemetry.addData("Back Right", velocitiesP.backRight)
                    telemetry.addLine(
                    "${round(driftV.first, 2)},${round(driftV.second, 2)},${round(driftV.third, 2)},${round(driftV.fourth,2)}")
                }
                telemetry.update()
            }
        }
    }
}