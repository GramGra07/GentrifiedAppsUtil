package org.gentrifiedApps.gentrifiedAppsUtil.drive

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Quadruple
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import kotlin.math.sign
import kotlin.test.Test
import kotlin.test.assertEquals

class MecanumTest {


    @Test
    fun assertAllEqual() {

        val drivePowerCoefficients = DrivePowerCoefficients.of(2.0)
        assert(drivePowerCoefficients.frontLeft == 2.0)
        assert(drivePowerCoefficients.frontRight == 2.0)
        assert(drivePowerCoefficients.backLeft == 2.0)
        assert(drivePowerCoefficients.backRight == 2.0)
    }

    @Test
    fun assertSigns() {
        val signs = Quadruple<Double>(1.0)
        val drivePowerCoefficients = DrivePowerCoefficients.of(1.0)
        // and PRINT
        println("DrivePowerCoefficients: $drivePowerCoefficients")
        println("Signs: $signs")
        assert(drivePowerCoefficients.frontLeft.sign == signs.first)
        assert(drivePowerCoefficients.frontRight.sign == signs.second)
        assert(drivePowerCoefficients.backLeft.sign == signs.third)
        assert(drivePowerCoefficients.backRight.sign == signs.fourth)
    }

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