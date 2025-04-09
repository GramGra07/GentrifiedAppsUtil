package test.kotlin.drive

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.drive.MecanumDriver
import org.junit.Test
import org.testng.Assert.assertEquals

class MecanumTest {

    @Test
    fun testDriveMecanum() {
        val drivePowerCoefficients = MecanumDriver.driveMecanum(0f, 1f, 0f)
        assertEquals(DrivePowerCoefficients(1.0, 1.0, 1.0, 1.0), drivePowerCoefficients)
    }
    @Test
    fun testDriveMecanum_Forward() {
        val drivePowerCoefficients = MecanumDriver.driveMecanum(0F, 1F, 0F)
        assertEquals(DrivePowerCoefficients(1.0, 1.0, 1.0, 1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveMecanum_Backward() {
        val drivePowerCoefficients = MecanumDriver.driveMecanum(0F, -1F, 0F)
        assertEquals(DrivePowerCoefficients(-1.0, -1.0, -1.0, -1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveMecanum_Right() {
        val drivePowerCoefficients = MecanumDriver.driveMecanum(1F, 0f, 0F)
        assertEquals(DrivePowerCoefficients(1.0, -1.0, -1.0, 1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveMecanum_Left() {
        val drivePowerCoefficients = MecanumDriver.driveMecanum(-1F, 0F, 0F)
        assertEquals(DrivePowerCoefficients(-1.0, 1.0, 1.0, -1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveMecanum_Rotation() {
        val drivePowerCoefficients = MecanumDriver.driveMecanum(0f, 0f, 1f)
        assertEquals(DrivePowerCoefficients(1.0, -1.0, 1.0, -1.0), drivePowerCoefficients)
    }

    @Test
    fun testDriveMecanum_Diagonal() {
        val drivePowerCoefficients = MecanumDriver.driveMecanum(1f, 1f, 0f)
        assertEquals(DrivePowerCoefficients(2.0, 0.0, 0.0, 2.0), drivePowerCoefficients)
    }
}