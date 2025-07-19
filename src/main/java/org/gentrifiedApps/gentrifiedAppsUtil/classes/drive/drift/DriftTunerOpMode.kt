package org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.round
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver

class DriftTunerOpMode @JvmOverloads constructor(
    val driver: Driver,
    val totalTime: Int = 5,
    val threshold: Double = 0.1
) : LinearOpMode() {
    constructor(driver: Driver, threshold: Double = 0.1) : this(driver, 5, threshold)

    override fun runOpMode() {
        Scribe.instance.startLogger(this)
        require(threshold > 0 && threshold < 0.9) {
            Scribe.instance.setSet("Drift Tuner").logError("Threshold must be between 0 and 0.9")
            "Threshold must be between 0 and 0.9"
        }
        driver.setupOpMode(this)
        driver.resetDriveEncoders()
        val timer = ElapsedTime()
        waitForStart()
        timer.reset()
        if (opModeIsActive()) {
            while (opModeIsActive() && !isStopRequested) {
                Scribe.instance.setSet("Drift Tuner").logDebug("${timer.seconds()}, $totalTime")
                if (timer.seconds() >= totalTime) {
                    break
                }
                driver.setWheelPower(DrivePowerCoefficients(1.0))
            }
            driver.setWheelPower(DrivePowerCoefficients(0.0))
            val velocitiesP = driver.getAbsPositions().asPercent()
            val driftV = velocitiesP.applyDriftNormalizer(threshold)
            if (driftV.all0()) {
                Scribe.instance.setSet("Drift Tuner").logDebug("No drift detected")
            } else {
                Scribe.instance.setSet("Drift Tuner").logDebug(
                    "Drift detected: ${velocitiesP.frontLeft}, ${velocitiesP.frontRight}, ${velocitiesP.backLeft}, ${velocitiesP.backRight}"
                )
                Scribe.instance.setSet("Drift Tuner").logDebug(
                    "Drift Constraints: ${driftV.first}, ${driftV.second}, ${driftV.third}, ${driftV.fourth}"
                )
            }
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
                        "${round(driftV.first, 2)},${round(driftV.second, 2)},${
                            round(
                                driftV.third,
                                2
                            )
                        },${round(driftV.fourth, 2)}"
                    )
                }
                telemetry.update()
            }
        }
    }
}