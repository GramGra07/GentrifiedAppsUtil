package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathComponent
import org.gentrifiedApps.gentrifiedAppsUtil.motion.controllers.PIDFController

class Heatseeker(
    private val driver: Driver,
    private var xPID: PIDFController,
    private var yPID: PIDFController,
    private var hPID: PIDFController
) {
    fun setPIDControllers(
        xPID: PIDFController,
        yPID: PIDFController,
        hPID: PIDFController
    ) {
        this.xPID = xPID
        this.yPID = yPID
        this.hPID = hPID
    }

    private var pathComponent = mutableListOf<PathComponent>()

    private var currentIndex = 0

//    private val feedforward = FeedforwardController(0.1, 0.05, 0.01)

//    fun followPath(path: List<Path>, tolerance: Double = 3.0) {
//        Scribe.instance.setSet("HS").logDebug("Following path")
//        require(driver.localizer != null)
//        this.path = path.map { it.waypoint() }.toMutableList()
//        driver.drawer.drawPath(this.path)
//        while (!isFinished(path) && driver.opMode!!.opModeIsActive() && !driver.opMode!!.isStopRequested) {
//            if (path.notFinished(currentIndex)) {
//                driver.update()
//                val current = driver.getCurrentPose()
//                val pNext = path[currentIndex]
//                val pT = pNext.target
//                val pVelo = pNext.velocity
//                val error = pT - current
//                val errorA = pT - current
//                when (pNext.type) {
//                    PathType.MOVE_TO -> {
//                        moveTo(error, errorA.angle, pVelo)
//                    }
//
//                    PathType.TURN_TO -> {
//                        turnTo(errorA.angle.norm(), pVelo)
//                    }
//                }
//                driver.update()
//                val current2 = driver.getCurrentPose()
//                if (pT.distanceTo(current2) < tolerance) {
//                    currentIndex++
//                    xPID.reset()
//                    yPID.reset()
//                    hPID.reset()
//                }
//            } else {
//                driver.setWheelPower(DrivePowerCoefficients.zeros())
//                break
//            }
//        }
//        driver.setWheelPower(DrivePowerCoefficients.zeros())
//    }
//
//    fun moveTo(error: Target2D, errorA: Angle, velo: Double) {
//        val xComp = error.x
//        val yComp = error.y
//        val xPower = xPID.calculate(xComp, 0.0)
//        val yPower = yPID.calculate(yComp, 0.0)
//        val hPower = hPID.calculate(errorA.toRadians(), 0.0)
//        driver.setWheelPower(driver.findWheelVectors(xPower, yPower, hPower).clip(-velo, velo))
//    }
//
//    fun turnTo(errorA: Angle, velo: Double) {
//        val hPower = hPID.calculate(errorA.toRadians(), 0.0)
//        driver.setWheelPower(driver.findWheelVectors(0.0, 0.0, hPower).clip(-velo, velo))
//
//    }
//
//    fun isFinished(path: List<Path>): Boolean {
//        return currentIndex >= path.size
//    }
//
//    fun teleOpCorrector(): TeleOpCorrector {
//        return TeleOpCorrector(driver)
//    }
}

//class TeleOpCorrector(private val driver: Driver) {
//    private var correctionAngle: Angle = Angle.blank()
//
//    init {
//        updateOrientation()
//        require(driver.localizer != null)
//    }
//
//    fun correctByAngle(drivePowerCoefficients: DrivePowerCoefficients): DrivePowerCoefficients {
//        require(driver.localizer != null)
//        driver.updatePoseEstimate()
//        val angleDifference =
//            driver.localizer!!.getPose().angle.toRadians() - correctionAngle.toRadians()
//        val frontLeft =
//            drivePowerCoefficients.frontLeft * cos(angleDifference) - drivePowerCoefficients.frontRight * sin(
//                angleDifference
//            )
//        val frontRight =
//            drivePowerCoefficients.frontLeft * sin(angleDifference) + drivePowerCoefficients.frontRight * cos(
//                angleDifference
//            )
//        val backLeft =
//            drivePowerCoefficients.backLeft * cos(angleDifference) - drivePowerCoefficients.backRight * sin(
//                angleDifference
//            )
//        val backRight =
//            drivePowerCoefficients.backLeft * sin(angleDifference) + drivePowerCoefficients.backRight * cos(
//                angleDifference
//            )
//        val newPowerCoefficients =
//            DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
//        return newPowerCoefficients
//    }
//
//    fun updateOrientation() {
//        correctionAngle = driver.localizer!!.getPose().angle
//    }
//}