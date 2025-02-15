package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.FeedforwardController
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PIDController
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class Heatseeker(private val driver: Driver,
                 private var xPID:PIDController, private var yPID:PIDController, private var hPID:PIDController) {
    fun setPIDControllers(xPID:PIDController,yPID:PIDController,hPID:PIDController){
        this.xPID = xPID
        this.yPID = yPID
        this.hPID = hPID
    }
    private var path = mutableListOf<Waypoint>()

    private var currentIndex = 0

    private val feedforward = FeedforwardController(0.1, 0.05, 0.01)

    fun followPath(path: List<Waypoint>, tolerance: Double) {
        this.path = path.toMutableList()
        driver.drawer.drawPath(path)
        while (!isFinished(path) && driver.opMode.opModeIsActive() && !driver.opMode.isStopRequested) {
            if (currentIndex >= path.size) {
                driver.setWheelPower(DrivePowerCoefficients.zeros())
                break
            }
            driver.update()

            val target = path[currentIndex]


            val error = driver.localizer.getPoseError(target.target2D)

            val xCorrection = xPID.calculate(error.x)
            val yCorrection = yPID.calculate(error.y)
            val headingCorrection = hPID.calculate(error.h())

            // Estimate desired velocity based on error
            val targetVelX = error.x * 0.5  // Scale for smooth movement
            val targetVelY = error.y * 0.5
            val targetVelH = error.h() * 0.5

            // Estimate acceleration assuming simple scaling
            val targetAccelX = targetVelX * 0.1
            val targetAccelY = targetVelY * 0.1
            val targetAccelH = targetVelH * 0.1

            // Compute feedforward terms
            val xFF = feedforward.calculate(targetVelX, targetAccelX)
            val yFF = feedforward.calculate(targetVelY, targetAccelY)
            val headingFF = feedforward.calculate(targetVelH, targetAccelH)

            // Combine PID and feedforward
            val xPower = xCorrection + xFF
            val yPower = yCorrection + yFF
            val headingPower = headingCorrection + headingFF

            val powerCoefficients = driver.findWheelVectors(yPower, xPower, headingPower)

            driver.setWheelPower(powerCoefficients)

            // Move to next waypoint if close enough
            if (hypot(error.x, error.y) < tolerance) {
                currentIndex++
            }
        }
        driver.setWheelPower(DrivePowerCoefficients.zeros())
    }

    fun isFinished(path: List<Waypoint>): Boolean {
        return currentIndex >= path.size
    }
    fun teleOpCorrector():TeleOpCorrector{
        return TeleOpCorrector(driver)
    }
}

class TeleOpCorrector(private val driver: Driver){
    private var correctionAngle :Angle = Angle.blank()
    init {
        updateOrientation()
    }
    fun correctByAngle(drivePowerCoefficients: DrivePowerCoefficients):DrivePowerCoefficients{
        val angleDifference = driver.localizer.getPose().angle.toRadians() - correctionAngle.toRadians()
        val frontLeft = drivePowerCoefficients.frontLeft * cos(angleDifference) - drivePowerCoefficients.frontRight * sin(angleDifference)
        val frontRight = drivePowerCoefficients.frontLeft * sin(angleDifference) + drivePowerCoefficients.frontRight * cos(angleDifference)
        val backLeft = drivePowerCoefficients.backLeft * cos(angleDifference) - drivePowerCoefficients.backRight * sin(angleDifference)
        val backRight = drivePowerCoefficients.backLeft * sin(angleDifference) + drivePowerCoefficients.backRight * cos(angleDifference)
        val newPowerCoefficients = DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
        return newPowerCoefficients
    }
    fun updateOrientation(){
        correctionAngle = driver.localizer.getPose().angle
    }
}