package test.kotlin.drive

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.drive.TankDriver
import org.junit.Test
import org.testng.Assert.assertEquals

class TankTest {
    @Test
    fun testDriveTank_Forward() {
        val drivePowerCoefficients = TankDriver.driveTank(1.0, 1.0)
        assertEquals(DrivePowerCoefficients(1.0, 1.0, 1.0, 1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveTank_Backward() {
        val drivePowerCoefficients = TankDriver.driveTank(-1.0, -1.0)
        assertEquals(DrivePowerCoefficients(-1.0, -1.0, -1.0, -1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveTank_TurnLeft() {
        val drivePowerCoefficients = TankDriver.driveTank(-1.0, 1.0)
        assertEquals(DrivePowerCoefficients(-1.0, 1.0, -1.0, 1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveTank_TurnRight() {
        val drivePowerCoefficients = TankDriver.driveTank(1.0, -1.0)
        assertEquals(DrivePowerCoefficients(1.0, -1.0, 1.0, -1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveTankRobotCentric_Forward() {
        val drivePowerCoefficients = TankDriver.driveTankRobotCentric(1.0, 0.0)
        assertEquals(DrivePowerCoefficients(1.0, 1.0, 1.0, 1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveTankRobotCentric_Rotate() {
        val drivePowerCoefficients = TankDriver.driveTankRobotCentric(0.0, 1.0)
        assertEquals(DrivePowerCoefficients(-1.0, 1.0, -1.0, 1.0), drivePowerCoefficients)
    }
}