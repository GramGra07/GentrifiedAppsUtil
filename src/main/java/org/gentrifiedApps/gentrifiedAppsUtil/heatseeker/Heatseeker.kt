package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PIDController
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint
import kotlin.math.hypot

class Heatseeker(private val driver: Driver) {
    private var path = mutableListOf<Waypoint>()

    private var currentIndex = 0

    private val xPID = PIDController(1.0, 0.0, 0.0)
    private val yPID = PIDController(1.0, 0.0, 0.0)
    private val hPID = PIDController(1.0, 0.0, 0.0)

    fun followPath(path: List<Waypoint>, tolerance: Double) {
        this.path = path.toMutableList()
        while (!isFinished(path) && driver.opMode.opModeIsActive() && !driver.opMode.isStopRequested) {
            if (currentIndex >= path.size) {
                driver.setWheelPower(DrivePowerCoefficients.zeros())
                break
            }
            driver.update()

            val target = path[currentIndex]


            val error = driver.localizer.getPoseError(target.target2D)

            // Compute PID corrections
            val xCorrection = xPID.calculatePID(error.x, 0.0) * target.velocity
            val yCorrection = yPID.calculatePID(error.y, 0.0) * target.velocity
            val headingCorrection = hPID.calculatePID(error.h(), 0.0) * target.velocity

            // Apply movement corrections
            val powerCoefficients =
                driver.findWheelVectors(yCorrection, xCorrection, headingCorrection)
            driver.setWheelPower(powerCoefficients)

            // Move to next waypoint if close enough
            if (hypot(error.x, error.y) < tolerance) {
                currentIndex++
            }
        }
        driver.setWheelPower(DrivePowerCoefficients.zeros())
    }

    private fun isFinished(path: List<Waypoint>): Boolean {
        return currentIndex >= path.size
    }
}