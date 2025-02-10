package test.kotlin

import org.gentrifiedApps.gentrifiedAppsUtil.drive.FieldCentricDriver
import org.gentrifiedApps.gentrifiedAppsUtil.drive.FieldCentricCoefficients
import org.junit.Test
import org.testng.Assert.assertEquals
import kotlin.math.sqrt

class FieldCentricTest {

    @Test
    fun testDriveFieldCentric() {
        val x = 0.0
        val y = 1.0
        val rotation = 0.0
        val gyroAngle = Math.toRadians(90.0)

        val result: FieldCentricCoefficients = FieldCentricDriver.driveFieldCentric(x, y, rotation, gyroAngle)

        assertEquals(1.0, result.frontLeft, "Front left power should be 1.0")
        assertEquals(1.0, result.frontRight, "Front right power should be 1.0")
        assertEquals(1.0, result.backLeft, "Back left power should be 1.0")
        assertEquals(1.0, result.backRight, "Back right power should be 1.0")
    }

    @Test
    fun testDriveFieldCentricWithRotation() {
        val x = 0.0
        val y = 0.0
        val rotation = 1.0
        val gyroAngle = 0.0

        val result: FieldCentricCoefficients = FieldCentricDriver.driveFieldCentric(x, y, rotation, gyroAngle)

        assertEquals(-1.0, result.frontLeft, "Front left power should be 0.0")
        assertEquals(1.0, result.frontRight, "Front right power should be 2.0")
        assertEquals(-1.0, result.backLeft, "Back left power should be 0.0")
        assertEquals(1.0, result.backRight, "Back right power should be 2.0")
    }

    @Test
    fun testDriveFieldCentricWithGyroAngle() {
        val x = 0.0
        val y = 0.0
        val rotation = 0.0
        val gyroAngle = Math.PI / 2

        val result: FieldCentricCoefficients = FieldCentricDriver.driveFieldCentric(x, y, rotation, gyroAngle)

        assertEquals(0.0, result.frontLeft, "Front left power should be 0.0")
        assertEquals(0.0, result.frontRight, "Front right power should be 0.0")
        assertEquals(0.0, result.backLeft, "Back left power should be 0.0")
        assertEquals(0.0, result.backRight, "Back right power should be 0.0")
    }
    private val delta = 0.001 // Tolerance for floating-point comparisons

    @Test
    fun testDriveFieldCentric_ZeroInput() {
        val result = FieldCentricDriver.driveFieldCentric(0.0, 0.0, 0.0, 0.0)
        assertEquals(0.0, result.frontLeft, delta)
        assertEquals(0.0, result.frontRight, delta)
        assertEquals(0.0, result.backLeft, delta)
        assertEquals(0.0, result.backRight, delta)
    }

    @Test
    fun testDriveFieldCentric_Forward() {
        val result = FieldCentricDriver.driveFieldCentric(0.0, 1.0, 0.0, Math.toRadians(90.0))
        assertEquals(1.0, result.frontLeft)
        assertEquals(1.0, result.frontRight)
        assertEquals(1.0, result.backLeft)
        assertEquals(1.0, result.backRight)
    }

    @Test
    fun testDriveFieldCentric_RightStrafe() {
        val result = FieldCentricDriver.driveFieldCentric(1.0, 0.0, 0.0, Math.toRadians(90.0))
        assertEquals(1.0, result.frontRight, delta)
        assertEquals(-1.0, result.backRight, delta)
        assertEquals(-1.0, result.frontLeft, delta)
        assertEquals(1.0, result.backLeft, delta)
    }

    @Test
    fun testDriveFieldCentric_DiagonalMovement() {
        var result = FieldCentricDriver.driveFieldCentric(1.0, 1.0, 0.0, Math.toRadians(90.0))
        println(result.frontRight)
        println(result.backRight)
        println(result.frontLeft)
        println(result.backLeft)
        assertEquals(1.0, result.frontRight)
        assertEquals(1.0, result.backLeft)
        result = FieldCentricDriver.driveFieldCentric(1.0, -1.0, 0.0, Math.toRadians(90.0))
        println(result.frontRight)
        println(result.backRight)
        println(result.frontLeft)
        println(result.backLeft)
        assertEquals(-1.0, result.frontLeft)
        assertEquals(-1.0, result.backRight)
    }

    @Test
    fun testDriveFieldCentric_RotationOnly() {
        val result = FieldCentricDriver.driveFieldCentric(0.0, 0.0, 1.0, 0.0)
        assertEquals(-1.0, result.frontLeft, delta)
        assertEquals(1.0, result.frontRight, delta)
        assertEquals(-1.0, result.backLeft, delta)
        assertEquals(1.0, result.backRight, delta)
    }
}