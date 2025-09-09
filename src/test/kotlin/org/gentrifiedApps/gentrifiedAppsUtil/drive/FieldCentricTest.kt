package org.gentrifiedApps.gentrifiedAppsUtil.drive

import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.AngleUnit
import kotlin.test.Test
import kotlin.test.assertEquals

class FieldCentricTest {

    @Test
    fun testDriveFieldCentric() {
        val x = 0.0
        val y = 1.0
        val rotation = 0.0
        val gyroAngle = Angle(90.0, AngleUnit.DEGREES)

        val result: DrivePowerCoefficients =
            FieldCentricDriver.driveFieldCentric(x, y, rotation, gyroAngle)

        assertEquals(-1.0, result.frontLeft, "Front left power should be 1.0")
        assertEquals(1.0, result.frontRight, "Front right power should be 1.0")
        assertEquals(1.0, result.backLeft, "Back left power should be 1.0")
        assertEquals(-1.0, result.backRight, "Back right power should be 1.0")
    }

    @Test
    fun testDriveFieldCentricBack() {
        val x = 0.0
        val y = -1.0
        val rotation = 0.0
        val gyroAngle = Angle(90.0, AngleUnit.DEGREES)

        val result: DrivePowerCoefficients =
            FieldCentricDriver.driveFieldCentric(x, y, rotation, gyroAngle)

        assertEquals(1.0, result.frontLeft)
        assertEquals(-1.0, result.frontRight)
        assertEquals(-1.0, result.backLeft)
        assertEquals(1.0, result.backRight)
    }

    @Test
    fun testDriveFieldCentricWithRotation() {
        val x = 0.0
        val y = 0.0
        val rotation = 1.0
        val gyroAngle = Angle(0.0, AngleUnit.DEGREES)

        val result: DrivePowerCoefficients =
            FieldCentricDriver.driveFieldCentric(x, y, rotation, gyroAngle)

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
        val gyroAngle = Angle(90.0, AngleUnit.DEGREES)

        val result: DrivePowerCoefficients =
            FieldCentricDriver.driveFieldCentric(x, y, rotation, gyroAngle)

        assertEquals(-0.0, result.frontLeft, "Front left power should be 0.0")
        assertEquals(0.0, result.frontRight, "Front right power should be 0.0")
        assertEquals(0.0, result.backLeft, "Back left power should be 0.0")
        assertEquals(0.0, result.backRight, "Back right power should be 0.0")
    }

    private val delta = 0.001 // Tolerance for floating-point comparisons

    @Test
    fun testDriveFieldCentric_ZeroInput() {
        val result =
            FieldCentricDriver.driveFieldCentric(0.0, 0.0, 0.0, Angle(0.0, AngleUnit.DEGREES))
        assertEquals(0.0, result.frontLeft, delta)
        assertEquals(0.0, result.frontRight, delta)
        assertEquals(0.0, result.backLeft, delta)
        assertEquals(0.0, result.backRight, delta)
    }

    @Test
    fun testDriveFieldCentric_Forward() {
        val result =
            FieldCentricDriver.driveFieldCentric(0.0, 1.0, 0.0, Angle(90.0, AngleUnit.DEGREES))
        assertEquals(-1.0, result.frontLeft)
        assertEquals(1.0, result.frontRight)
        assertEquals(1.0, result.backLeft)
        assertEquals(-1.0, result.backRight)
    }

    @Test
    fun testDriveFieldCentric_RightStrafe() {
        val result =
            FieldCentricDriver.driveFieldCentric(1.0, 0.0, 0.0, Angle(90.0, AngleUnit.DEGREES))
        assertEquals(-1.0, result.frontRight, delta)
        assertEquals(-1.0, result.backRight, delta)
        assertEquals(-1.0, result.frontLeft, delta)
        assertEquals(-1.0, result.backLeft, delta)
    }

    @Test
    fun testDriveFieldCentric_DiagonalMovement() {
        var result =
            FieldCentricDriver.driveFieldCentric(1.0, 1.0, 0.0, Angle(90.0, AngleUnit.DEGREES))
        println(result.frontRight)
        println(result.backRight)
        println(result.frontLeft)
        println(result.backLeft)
        assertEquals(0.0, result.frontRight, 1e-5)
        assertEquals(.0, result.backLeft, 1e-5)
        result =
            FieldCentricDriver.driveFieldCentric(1.0, -1.0, 0.0, Angle(90.0, AngleUnit.DEGREES))
        println(result.frontRight)
        println(result.backRight)
        println(result.frontLeft)
        println(result.backLeft)
        assertEquals(0.0, result.frontLeft, 1e-5)
        assertEquals(0.0, result.backRight, 1e-5)
    }

    @Test
    fun testDriveFieldCentric_RotationOnly() {
        val result =
            FieldCentricDriver.driveFieldCentric(0.0, 0.0, 1.0, Angle(0.0, AngleUnit.DEGREES))
        assertEquals(-1.0, result.frontLeft, delta)
        assertEquals(1.0, result.frontRight, delta)
        assertEquals(-1.0, result.backLeft, delta)
        assertEquals(1.0, result.backRight, delta)
    }
}