package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import com.qualcomm.robotcore.util.ElapsedTime
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.Callback
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.Path
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathCallback
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathComponent
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathType
import org.gentrifiedApps.gentrifiedAppsUtil.motion.controllers.PIDFController
import kotlin.math.cos
import kotlin.math.sin

class Heatseeker(
    private val driver: Driver,
) {
    val timer: ElapsedTime = ElapsedTime()

    private fun log(message: String) {
        Scribe.instance.setSet("HS").logData(message)
    }

    lateinit var path: Path
    private var xPID = PIDFController()
    private var yPID = PIDFController()
    private var hPID = PIDFController()

    fun setHSPath(path: Path) {
        log("New path set")
        this.path = path
        driver.drawer.drawPath(path.waypoints)
    }

    private fun components(): MutableList<PathComponent> {
        return path.waypoints
    }

    private fun callbacks(): MutableList<Callback> {
        return path.callbacks
    }

    fun setPIDControllers(
        xPID: PIDFController,
        yPID: PIDFController,
        hPID: PIDFController
    ) {
        this.xPID = xPID
        this.yPID = yPID
        this.hPID = hPID
    }

    private var currentIndex = 0

    val components = components()
    val callbacks = callbacks()
    var distance = 0.0
    fun followPath(tolerance: Double = 3.0, inTele: Boolean = false) {
        require(driver.hasLocalizer())
        log("Following path $currentIndex")
        if (distance == 0.0) timer.reset()
        if (path.isFinished(currentIndex) && driver.opMode!!.opModeIsActive() && !driver.opMode!!.isStopRequested) {
            // check callbacks
            if (!inTele) {
                for (callback in callbacks) {
                    if (callback.atDistance(distance)) {
                        callback.run()
                        callbacks.remove(callback)
                        log("Ran distance callback")
                    } else if (callback.atTime(timer.seconds())) {
                        callback.run()
                        callbacks.remove(callback)
                        log("Ran time callback")
                    }
                }
            }
            val cPath = components[currentIndex]
            val type = cPath.type
            val v = cPath.velocity
            val target = cPath.target
            val current = driver.getCurrentPose()
            when (type) {
                PathType.MOVE_TO,
                PathType.TURN_TO -> {
                    val pVec = current.pVecTo(target)
                    val xPower = xPID.calculate(pVec.translationalVector.a)
                    val yPower = yPID.calculate(pVec.translationalVector.b)
                    val hPower = hPID.calculate(pVec.rotationalVector.angle)
                    val wheelVectors = driver.findWheelVectors(xPower, yPower, hPower) * v
                    driver.setWheelPower(wheelVectors.clip(-v, v))
                }

                PathType.CALLBACK -> {
                    (cPath as PathCallback).callback.run()
                    log("Callback run")
                }
            }

            if (type != PathType.CALLBACK) {
                currentIndex++
            }
            val error = target.distanceTo(current)
            if (error < tolerance) {
                distance += driver.getPoseDiff()
            }
            driver.update()
            xPID.reset()
            yPID.reset()
            hPID.reset()
        }
        if (path.isFinished(currentIndex) || driver.opMode!!.isStopRequested || !driver.opMode!!.opModeIsActive()) {
            driver.setWheelPower(DrivePowerCoefficients.zeros())
            log("Finished following path")
        }
    }

    fun teleOpCorrector(): TeleOpCorrector {
        return TeleOpCorrector(driver)
    }
}

class TeleOpCorrector @JvmOverloads constructor(
    private val driver: Driver,
    var correctionAngle: Angle = Angle.blank()
) {

    init {
        updateOrientation()
        require(driver.localizer != null)
    }

    fun correctByAngle(drivePowerCoefficients: DrivePowerCoefficients): DrivePowerCoefficients {
        require(driver.localizer != null)
        driver.getCurrentPose()
        val angleDifference =
            driver.localizer!!.getPose().angle.toRadians() - correctionAngle.toRadians()
        val frontLeft =
            drivePowerCoefficients.frontLeft * cos(angleDifference) - drivePowerCoefficients.frontRight * sin(
                angleDifference
            )
        val frontRight =
            drivePowerCoefficients.frontLeft * sin(angleDifference) + drivePowerCoefficients.frontRight * cos(
                angleDifference
            )
        val backLeft =
            drivePowerCoefficients.backLeft * cos(angleDifference) - drivePowerCoefficients.backRight * sin(
                angleDifference
            )
        val backRight =
            drivePowerCoefficients.backLeft * sin(angleDifference) + drivePowerCoefficients.backRight * cos(
                angleDifference
            )
        val newPowerCoefficients =
            DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
        return newPowerCoefficients
    }

    fun updateOrientation() {
        correctionAngle = driver.localizer!!.getPose().angle
    }

    fun updateOrientation(orientation: Angle) {
        correctionAngle = orientation
    }
}