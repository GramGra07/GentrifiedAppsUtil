package test.kotlin.classes.avoidance

import junit.framework.TestCase.assertEquals
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Quadruple
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.avoidance.AvoidanceController
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.avoidance.AvoidanceVectorType
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.avoidance.VectorField
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point
import org.junit.Test

class AvoidanceControllerTest {

    @Test
    fun testUpdatePointInField() {
        val field = VectorField(Point(0.0, 0.0), 5.0, AvoidanceVectorType.BOTH)
        val controller = AvoidanceController(field)

        val point = Point(2.0, 2.0)
        val result = controller.update(point)

        DrivePowerCoefficients.TestCases.Companion.assertSigns(
            Quadruple<Double>(
                1.0,
                -1.0,
                -1.0,
                1.0
            ), result
        )
    }

    @Test
    fun testUpdatePointOutsideField() {
        val field = VectorField(Point(0.0, 0.0), 5.0, AvoidanceVectorType.BOTH)
        val controller = AvoidanceController(field)

        val point = Point(10.0, 10.0)
        val result = controller.update(point)

        assertEquals(DrivePowerCoefficients.zeros(), result)
    }

    @Test
    fun testInField() {
        val field1 = VectorField(Point(0.0, 0.0), 5.0, AvoidanceVectorType.BOTH)
        val field2 = VectorField(Point(10.0, 10.0), 5.0, AvoidanceVectorType.BOTH)
        val controller = AvoidanceController(field1, field2)

        val point = Point(10.0, 10.0)
        val result = controller.inField(point)

        assertEquals(field2, result)
    }

    @Test
    fun testAddField() {
        val field1 = VectorField(Point(0.0, 0.0), 5.0, AvoidanceVectorType.BOTH)
        val controller = AvoidanceController(field1)

        val field2 = VectorField(Point(10.0, 10.0), 5.0, AvoidanceVectorType.BOTH)
        controller.addField(field2)

        val point = Point(10.0, 10.0)
        val result = controller.inField(point)

        assertEquals(field2, result)
    }
}