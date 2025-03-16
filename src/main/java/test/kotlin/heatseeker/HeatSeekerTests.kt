package test.kotlin.heatseeker

import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generators.EncoderSpecsBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.Encoder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.EncoderSpecs
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.AngleUnit
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.point.OTOSLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.tracking.MecanumLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.path.Path
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.path.PathType
import org.junit.Test
import org.testng.Assert.assertEquals

class HeatSeekerTests {
}
class Target2DTests{
    @Test
    fun testTarget(){
        val target = Target2D.blank()
        assert(target.x == 0.0)
        assert(target.y == 0.0)
        assert(target.angle.angle == 0.0)
        val target2 = Target2D(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES))
        assert(target2.x == 1.0)
        assert(target2.y == 2.0)
        assert(target2.angle.angle == 3.0)
        assert(target.distanceTo(target2) == 2.23606797749979)
        assert(target.angleTo(target2) == 1.1071487177940904)
    }
    @Test
    fun testBlank() {
        val target = Target2D.blank()
        assert(target.x == 0.0)
        assert(target.y == 0.0)
        assert(target.angle.angle == 0.0)
    }
    @Test
    fun testDistanceTo() {
        val target = Target2D(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES))

        assert(target.distanceTo(Target2D(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES))) == 0.0)
        assert(target.distanceTo(Target2D(1.0, 3.0, Angle(3.0, AngleUnit.DEGREES))) == 1.0)
        assert(target.distanceTo(Target2D(1.0, 1.0, Angle(3.0, AngleUnit.DEGREES))) == 1.0)
        assert(target.distanceTo(Target2D(2.0, 2.0, Angle(3.0, AngleUnit.DEGREES))) == 1.0)
        assert(target.distanceTo(Target2D(0.0, 2.0, Angle(3.0, AngleUnit.DEGREES))) == 1.0)
    }
    @Test
    fun testAngleTo() {
        val target = Target2D(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES))
        assert(target.angleTo(Target2D(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES))) == 0.0)
        assert(target.angleTo(Target2D(1.0, 3.0, Angle(3.0, AngleUnit.DEGREES))) == Angle(90.0,
            AngleUnit.DEGREES).toRadians())
        assert(target.angleTo(Target2D(1.0, 1.0, Angle(3.0, AngleUnit.DEGREES))) == -Angle(90.0,
            AngleUnit.DEGREES).toRadians())
        assert(target.angleTo(Target2D(2.0, 2.0, Angle(3.0, AngleUnit.DEGREES))) == Angle(0.0,
            AngleUnit.DEGREES).toRadians())
        assert(target.angleTo(Target2D(0.0, 2.0, Angle(3.0, AngleUnit.DEGREES))) == Math.PI)
    }
}
class AngleTests{
    @Test
    fun testAngle() {
        val angle = Angle.blank()
        assert(angle.angle == 0.0)
        assert(angle.unit == AngleUnit.RADIANS)
        assert(angle.toRadians() == 0.0)
        assert(angle.toDegrees() == 0.0)
        assert(angle.toAngleUnit(AngleUnit.DEGREES).angle == 0.0)
        assert(angle.toAngleUnit(AngleUnit.RADIANS).angle == 0.0)
        assert(angle.angleWrap().angle == 0.0)
        assert(angle.errorTo(Angle(1.0, AngleUnit.DEGREES)).angle == 1.0)
        assert(angle.errorToD(Angle(1.0, AngleUnit.DEGREES)) == 1.0)
    }
    @Test
    fun testAngleWrap(){
        val angle = Angle(370.0, AngleUnit.DEGREES)
        assert(angle.angleWrap().angle == 10.0)
    }
    @Test
    fun testAngleWrapBackwards(){
        val angle = Angle(-10.0, AngleUnit.DEGREES)
        assert(angle.angleWrap().angle == 350.0)
    }
    @Test
    fun testAngleErrorTo() {
        val angle = Angle(370.0, AngleUnit.DEGREES)
        assert(angle.errorTo(Angle(10.0, AngleUnit.DEGREES)).angle == 0.0)
        assert(angle.errorToD(Angle(10.0, AngleUnit.DEGREES)) == 0.0)
    }
    @Test
    fun testAngleErrorToBackwards() {
        val angle = Angle(10.0, AngleUnit.DEGREES)
        println(angle.errorTo(Angle(370.0, AngleUnit.DEGREES)).angle)
        assert(angle.errorTo(Angle(370.0, AngleUnit.DEGREES)).angle == 0.0)
        assert(angle.errorToD(Angle(370.0, AngleUnit.DEGREES)) == 0.0)
    }
    @Test
    fun testAngleWrapReturnsCorrectUnit(){
        val angle = Angle(370.0, AngleUnit.DEGREES)
        assert(angle.angleWrap().unit == AngleUnit.DEGREES)
        assert(angle.angleWrap(180.0).unit == AngleUnit.DEGREES)
        assert(angle.angleWrap(360.0).unit == AngleUnit.DEGREES)
        assert(angle.angleWrap(720.0).unit == AngleUnit.DEGREES)
        assert(angle.angleWrap(1080.0).unit == AngleUnit.DEGREES)
        assert(angle.angleWrap(1440.0).unit == AngleUnit.DEGREES)
        assert(angle.angleWrap(2160.0).unit == AngleUnit.DEGREES)
        assert(angle.angleWrap(2880.0).unit == AngleUnit.DEGREES)
        val angle2 = Angle(370.0, AngleUnit.RADIANS)
        assert(angle2.angleWrap().unit == AngleUnit.RADIANS)
        assert(angle2.angleWrap(180.0).unit == AngleUnit.RADIANS)
        assert(angle2.angleWrap(360.0).unit == AngleUnit.RADIANS)
    }
    @Test
    fun testErrorToDegToRad(){
        val angle = Angle(370.0, AngleUnit.DEGREES)
        assert(angle.errorTo(Angle(10.0, AngleUnit.RADIANS)).angle == 0.0)
        assert(angle.errorToD(Angle(10.0, AngleUnit.RADIANS)) == 0.0)
    }
    @Test
    fun testToDegrees(){
        val angle = Angle(180.0, AngleUnit.DEGREES)
        assert(angle.toDegrees() == 180.0)
    }
    @Test
    fun testToRadians(){
        val angle = Angle(180.0, AngleUnit.DEGREES)
        assert(angle.toRadians() == Math.PI)
    }
}
class DriverTests{
    @Test
    fun testDriver(){
        val vectors = Driver.findWheelVectors(1.0,0.0,0.0)
        assert(vectors.frontLeft == 1.0)
        assert(vectors.frontRight == 1.0)
        assert(vectors.backLeft == 1.0)
        assert(vectors.backRight == 1.0)
        val vectors2 = Driver.findWheelVectors(0.0,1.0,0.0)
        assert(vectors2.frontLeft == 1.0)
        assert(vectors2.frontRight == -1.0)
        assert(vectors2.backLeft == -1.0)
        assert(vectors2.backRight == 1.0)
        val vectors3 = Driver.findWheelVectors(0.0,0.0,1.0)
    }
}
class WaypointTests{
    @Test
    fun testWaypointConstructor(){
        val waypoint = Waypoint(1.0,2.0,Math.toDegrees(3.0),1.0)
        assert(waypoint.x == 1.0)
        assert(waypoint.y == 2.0)
        assert(waypoint.h == 3.0)
        assert(waypoint.velocity == 1.0)
    }
    @Test
    fun testWaypointConstructor2(){
        val waypoint = Waypoint(1.0,2.0,Angle(3.0, AngleUnit.DEGREES),1.0)
        assert(waypoint.x == 1.0)
        assert(waypoint.y == 2.0)
        assert(waypoint.h == Math.toRadians(3.0))
        assert(waypoint.velocity == 1.0)
    }
    @Test
    fun testWaypointConstructor3() {
        val waypoint =
            Waypoint(
                Target2D(
                    1.0,
                    2.0,
                    Angle(3.0, AngleUnit.DEGREES)
                ), 1.0
            )

        assert(waypoint.x == 1.0)
        assert(waypoint.y == 2.0)
        assert(waypoint.h == Math.toRadians(3.0))
        assert(waypoint.velocity == 1.0)
    }
}
class PathBuilderTests{
    @Test
    fun testPathBuilderBasics(){
        val path = PathBuilder()
            .addWaypoint(Waypoint(1.0,2.0,Math.toDegrees(3.0),1.0))
            .addWaypoint(Waypoint(5.0,6.0,Math.toDegrees(7.0),1.0))
            .build()
        assert(path.size == 2)
        assert(path[0].x() == 1.0)
        assert(path[0].y() == 2.0)
        assert(path[0].h() == 3.0)
        assert(path[0].velocity == 1.0)
        assert(path[1].x() == 5.0)
        assert(path[1].y() == 6.0)
        assert(path[1].h() == 7.0)
        assert(path[1].velocity == 1.0)
    }
    @Test
    fun testPathConstructorWithWaypoint() {
        val waypoint = Waypoint(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES), 1.0)
        val path = Path(waypoint)
        assertEquals(PathType.MOVE_TO, path.type)
        assertEquals(1.0, path.x())
        assertEquals(2.0, path.y())
        assertEquals(Angle(3.0, AngleUnit.DEGREES).toRadians(), path.h())
        assertEquals(1.0, path.velocity)
    }

    @Test
    fun testPathConstructorWithTargetAndVelocity() {
        val target = Target2D(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES))
        val path = Path(target, 1.0)
        assertEquals(PathType.MOVE_TO, path.type)
        assertEquals(1.0, path.x())
        assertEquals(2.0, path.y())
        assertEquals(Angle(3.0, AngleUnit.DEGREES).toRadians(), path.h())
        assertEquals(1.0, path.velocity)
    }

    @Test
    fun testPathConstructorWithHeadingVelocityAndCurrentTarget() {
        val currentTarget = Target2D(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES))
        val path = Path(Angle(4.0, AngleUnit.DEGREES), 1.0, currentTarget)
        assertEquals(PathType.TURN_TO, path.type)
        assertEquals(1.0, path.x())
        assertEquals(2.0, path.y())
        assertEquals(Angle(4.0, AngleUnit.DEGREES).toRadians(), path.h())
        assertEquals(1.0, path.velocity)
    }

    @Test
    fun testPathConstructorWithHeadingVelocityXAndY() {
        val path = Path(Angle(4.0, AngleUnit.DEGREES), 1.0, 1.0, 2.0)
        assertEquals(PathType.TURN_TO, path.type)
        assertEquals(1.0, path.x())
        assertEquals(2.0, path.y())
        assertEquals(Angle(4.0, AngleUnit.DEGREES).toRadians(), path.h())
        assertEquals(1.0, path.velocity)
    }

    @Test
    fun testWaypointFunction() {
        val path = Path(Target2D(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES)), 1.0)
        val waypoint = path.waypoint()
        assertEquals(1.0, waypoint.x)
        assertEquals(2.0, waypoint.y)
        assertEquals(Angle(3.0, AngleUnit.DEGREES).toRadians(), waypoint.h)
        assertEquals(1.0, waypoint.velocity)
    }
}
class EncoderTests{
    @Test
    fun testEncoderSpecsBasic(){
        val encoderSpecs = EncoderSpecs(1, 1.0, 1.0)
        assert(encoderSpecs.ticksPerInch == 1.0 / Math.PI)
        val encoder = Encoder(encoderSpecs, "encoder", DcMotorSimple.Direction.FORWARD, 0.0,null)
        assert(encoder.getTicks() == 0)
        assert(encoder.getDelta() == 0)
        assert(encoder.getInches() == 0.0)
    }
    @Test
    fun testEncoderSpecs(){
        val encoderSpecs = EncoderSpecs(1, 1.0, 1.0)
        assert(encoderSpecs.ticksPerInch == 1.0 / Math.PI)
    }
    @Test
    fun testBuilders(){
        val encoderSpecs = EncoderSpecsBuilder.goBildaSwingArm()
        println(encoderSpecs.ticksPerInch)
        assert(encoderSpecs.ticksPerInch == 2000/(Math.PI*1.88976))
    }
}