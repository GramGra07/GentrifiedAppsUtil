package test.kotlin.classes.avoidance

import junit.framework.TestCase.assertEquals
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.avoidance.AvoidanceVectorType
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.avoidance.VectorField
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point
import org.junit.Test

class VectorFieldTest {

    @Test
    fun testInField() {
        val field = VectorField(Point(0.0, 0.0), 5.0, AvoidanceVectorType.BOTH)

        val insidePoint = Point(3.0, 4.0) // Distance = 5.0
        val outsidePoint = Point(6.0, 8.0) // Distance > 5.0

        assertEquals(true, field.inField(insidePoint))
        assertEquals(false, field.inField(outsidePoint))
    }

    @Test
    fun testFieldMovementVector() {
        val field = VectorField(Point(0.0, 0.0), 5.0, AvoidanceVectorType.BOTH)
        val point = Point(3.0, 4.0) // Distance = 5.0

        val vector = field.fieldMovementVector(point)

        assertEquals(0.0, vector.magnitude) // At the edge of the field, magnitude should be 0
    }


    @Test
    fun testCorrectionAsDrive_Off() {
        val field = VectorField(Point(0.0, 0.0), 5.0, AvoidanceVectorType.OFF)
        val point = Point(2.0, 2.0)

        val correction = field.correctionAsDrive(point)

        assertEquals(DrivePowerCoefficients.zeros(), correction)
    }
}