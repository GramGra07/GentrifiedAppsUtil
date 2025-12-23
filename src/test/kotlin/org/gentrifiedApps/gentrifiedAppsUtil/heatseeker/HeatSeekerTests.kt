package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DriveVelocities
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.AngleUnit
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Waypoint
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generators.EncoderSpecsBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.Encoder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.EncoderSpecs
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.Vector
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.localizers.tracking.MecanumLocalizer
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.path.Path
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.path.PathType
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DcMotorW
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.HWMapW
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.Test

class HeatSeekerTests {
}

class Target2DTests {
    @Test
    fun testTarget() {
        val target = Target2D.blank()
        assert(target.x == 0.0)
        assert(target.y == 0.0)
        assert(target.angle.toRadians() == Math.PI / 2)
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
        assert(target.angle.angle == Math.PI / 2)
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
        assert(
            target.angleTo(Target2D(1.0, 3.0, Angle(3.0, AngleUnit.DEGREES))) == Angle(
                90.0,
                AngleUnit.DEGREES
            ).toRadians()
        )
        assert(
            target.angleTo(Target2D(1.0, 1.0, Angle(3.0, AngleUnit.DEGREES))) == -Angle(
                90.0,
                AngleUnit.DEGREES
            ).toRadians()
        )
        assert(
            target.angleTo(Target2D(2.0, 2.0, Angle(3.0, AngleUnit.DEGREES))) == Angle(
                0.0,
                AngleUnit.DEGREES
            ).toRadians()
        )
        assert(target.angleTo(Target2D(0.0, 2.0, Angle(3.0, AngleUnit.DEGREES))) == Math.PI)
    }
}

class AngleTests {
    @Test
    fun testAngle() {
        val angle = Angle.blank()
        assert(angle.angle == Math.PI / 2)
        assert(angle.unit == AngleUnit.RADIANS)
        assert(angle.toRadians() == Math.PI / 2)
        assert(angle.toDegrees() == 90.0)
        assert(angle.toAngleUnit(AngleUnit.DEGREES).angle == 90.0)
        assert(angle.toAngleUnit(AngleUnit.RADIANS).angle == Math.PI / 2)
        assert(angle.angleWrap().angle == Math.PI / 2)
        println(angle.errorToD(Angle(1.0, AngleUnit.DEGREES)))
        assert(
            angle.errorTo(
                Angle(
                    1.0,
                    AngleUnit.DEGREES
                )
            ).angle == 1.0 - Math.PI / 2
        )
        assert(angle.errorToD(Angle(1.0, AngleUnit.DEGREES)) == 1 - Math.PI / 2)

    }

    @Test
    fun testAngleWrap() {
        val angle = Angle(370.0, AngleUnit.DEGREES)
        assert(angle.angleWrap().angle == 10.0)
    }

    @Test
    fun testAngleWrapBackwards() {
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
    fun testAngleWrapReturnsCorrectUnit() {
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
    fun testErrorToDegToRad() {
        val angle = Angle(370.0, AngleUnit.DEGREES)
        assert(angle.errorTo(Angle(10.0, AngleUnit.RADIANS)).angle == 0.0)
        assert(angle.errorToD(Angle(10.0, AngleUnit.RADIANS)) == 0.0)
    }

    @Test
    fun testToDegrees() {
        val angle = Angle(180.0, AngleUnit.DEGREES)
        assert(angle.toDegrees() == 180.0)
    }

    @Test
    fun testToRadians() {
        val angle = Angle(180.0, AngleUnit.DEGREES)
        assert(angle.toRadians() == Math.PI)
    }
}

class DriverTests {
    @Test
    fun testDriver() {
        val vectors = Driver.findWheelVectors(1.0, 0.0, 0.0)
        assert(vectors.frontLeft == 1.0)
        assert(vectors.frontRight == 1.0)
        assert(vectors.backLeft == 1.0)
        assert(vectors.backRight == 1.0)
        val vectors2 = Driver.findWheelVectors(0.0, 1.0, 0.0)
        assert(vectors2.frontLeft == 1.0)
        assert(vectors2.frontRight == -1.0)
        assert(vectors2.backLeft == -1.0)
        assert(vectors2.backRight == 1.0)
        val vectors3 = Driver.findWheelVectors(0.0, 0.0, 1.0)
        assert(vectors3.frontLeft == 1.0)
        assert(vectors3.frontRight == -1.0)
        assert(vectors3.backLeft == 1.0)
        assert(vectors3.backRight == -1.0)
        val vectorVec = Vector(0.0, 1.0, 0.0)
        val interpretedVector = Driver.findWheelVecs(vectorVec)
        assert(interpretedVector.frontLeft == 1.0)
        assert(interpretedVector.frontRight == 1.0)
        assert(interpretedVector.backLeft == 1.0)
        assert(interpretedVector.backRight == 1.0)
        val vectorVec2 = Vector(0.0, 0.0, 1.0)
        val interpretedVector2 = Driver.findWheelVecs(vectorVec2)
        assert(interpretedVector2.frontLeft == 1.0)
        assert(interpretedVector2.frontRight == -1.0)
        assert(interpretedVector2.backLeft == 1.0)
        assert(interpretedVector2.backRight == -1.0)
        val vectorVec3 = Vector(1.0, 0.0, 0.0)
        val interpretedVector3 = Driver.findWheelVecs(vectorVec3)
        assert(interpretedVector3.frontLeft == 1.0)
        assert(interpretedVector3.frontRight == -1.0)
        assert(interpretedVector3.backLeft == -1.0)
        assert(interpretedVector3.backRight == 1.0)
    }
}

class VectorTests {
    @Test
    fun testVectorCreation() {
        val v1 = Vector(1.0, 2.0, 0.0)
        val v2 = Vector(1.9, 2.0, 10.0)
        assert(v1.a != v2.a)
        assert(v1.b == v2.b)
        assert(v1.c != v2.c)
    }

    @Test
    fun testVectorMath() {
        val v1 = Vector(1.0, 1.0, 1.0)
        val v2 = Vector(2.0, 2.0, 1.0)
        val expectedAdd = Vector(3.0, 3.0, 2.0)
        assert(expectedAdd == v1 + v2)
        val expectedSub = Vector(-1.0, -1.0, 0.0)
        assert(expectedSub == v1 - v2)
        val expectedMultScale = Vector(2.0, 2.0, 2.0)
        assert(expectedMultScale == v1 * 2.0)
        val expectedCross = Vector(-1.0, 1.0, 0.0)
        assert(expectedCross == v1.crossProduct(v2))
        val expectedDot = 2 + 2 + 1.0
        assert(expectedDot == v1 * v2)
        val expectedDerivation = Vector(3.0, 4.0, 0.0)
        assert(
            expectedDerivation == Vector.of(
                Target2D(1.0, 2.0, Angle(90.0)),
                Target2D(4.0, 6.0, Angle(90.0))
            )
        )
        val expectedMagnitude = sqrt(1.0 + 1.0 + 1.0)
        assert(expectedMagnitude == v1.magnitude())
    }
}

class WaypointTests {
    @Test
    fun testWaypointConstructor() {
        val waypoint = Waypoint(1.0, 2.0, Angle.ofRadians(3.0), 1.0)
        assert(waypoint.x == 1.0)
        assert(waypoint.y == 2.0)
        assert(waypoint.h == 3.0)
        assert(waypoint.velocity == 1.0)
    }

    @Test
    fun testWaypointConstructor2() {
        val waypoint = Waypoint(1.0, 2.0, Angle(3.0, AngleUnit.DEGREES), 1.0)
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

class PathBuilderTests {
    @Test
    fun testPathBuilderBasics() {
        val path = PathBuilder()
            .addWaypoint(Waypoint(1.0, 2.0, Angle.ofRadians(3.0), 1.0))
            .addWaypoint(Waypoint(5.0, 6.0, Angle.ofRadians(7.0), 1.0))
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

class EncoderTests {
    @Test
    fun testEncoderSpecsBasic() {
        val encoderSpecs = EncoderSpecs(1, 1.0, 1.0)
        assert(encoderSpecs.ticksPerInch == 1.0 / Math.PI)
        val encoder = Encoder(encoderSpecs, "encoder", DcMotorSimple.Direction.FORWARD, null)
        assert(encoder.getTicks() == 0)
        assert(encoder.getDelta() == 0)
        assert(encoder.getInches() == 0.0)
    }

    @Test
    fun testEncoderSpecs() {
        val encoderSpecs = EncoderSpecs(1, 1.0, 1.0)
        assert(encoderSpecs.ticksPerInch == 1.0 / Math.PI)
    }

    @Test
    fun testBuilders() {
        val encoderSpecs = EncoderSpecsBuilder.goBildaSwingArm()
        println(encoderSpecs.ticksPerInch)
        assert(encoderSpecs.ticksPerInch == 2000 / (Math.PI * 1.88976))
    }
}

class GenericTests {
    @Test
    fun testEncoder() {
        val specs = EncoderSpecsBuilder.goBildaSwingArm()
        val hw = HWMapW()
        val e = Encoder(specs, "testMotor", DcMotorSimple.Direction.FORWARD, false, 0.0, null)
        e.encoder = DcMotorW("testMotor", 1)
        assert(e.getTicks() == 0)
        assert(e.getDelta() == 0)
        assert(e.getDeltaInches() == 0.0)
        assert(e.ticksPerIn() == 2000.0 / (1.88976 * Math.PI))
        e.encoder!!.power = 1.0
        assert(e.getTicks() == 10)
        assert(e.getInches() == 10 / specs.ticksPerInch)
        assert(e.getDelta() == 10)
        e.setLastPosition()
        e.encoder!!.power = -1.0
        assert(e.getTicks() == 0)
        assert(e.getDelta() == -10)
        assert(e.getDeltaInches() == -10.0 / specs.ticksPerInch)
        assert(e.getInches() == 0.0)
        e.reset()
        assert(e.getTicks() == 0)
        assert(e.getDelta() == 0)
        e.setHardReverse(true)
        e.encoder!!.power = 1.0
        assert(e.getTicks() == -10)
    }

    @Test
    fun testDriver() {
        val fl = DcMotorW("fl", 2)
        val fr = DcMotorW("fr", 2)
        val br = DcMotorW("br", 2)
        val bl = DcMotorW("bl", 2)

        val d =
            Driver().aconstructor(fl, fr, bl, br)
        assert(d.hwMap == null)
        assert(d.getPositions() == DriveVelocities.zeros())
        assert(d.getAbsPositions() == DriveVelocities.zeros())
        val straight = Driver.findWheelVectors(1.0, 0.0, 0.0)
        d.setWheelPower(straight)
        assert(d.getPositions() == DriveVelocities.of(10.0))
        val back = Driver.findWheelVectors(-0.8, 0.0, 0.0)
        d.setWheelPower(back)
        assert(d.getPositions() == DriveVelocities.of(2.0))
        d.resetDriveEncoders()
        assert(d.getPositions() == DriveVelocities.zeros())
        assert(
            d.sendEncoders() == listOf(
                Pair<DcMotor, String>(fl, ""),
                Pair<DcMotor, String>(fr, ""),
                Pair<DcMotor, String>(bl, ""),
                Pair<DcMotor, String>(br, "")
            )
        )
        assert(d.localizer == null)
    }

    @Test
    fun testMecLocalizer() {

        val fl = DcMotorW("fl", 2)
        val fr = DcMotorW("fr", 2)
        val br = DcMotorW("br", 2)
        val bl = DcMotorW("bl", 2)

        val d = Driver().aconstructor(fl, fr, bl, br)
        val specs = EncoderSpecsBuilder.goBildaSwingArm()
        val m = MecanumLocalizer(d, specs.ticksPerInch, 10.0, Target2D.blank())
        assert(m.getPose() == Target2D.blank())
        d.setWheelPower(DrivePowerCoefficients.of(1.0))
        assert(m.getPose() == Target2D.blank())
        m.update()
        assert(m.getPose() != Target2D.blank())
        assert(abs(m.getPose().x) < 0.005)
        assert(m.getPose().y == 10.0 / specs.ticksPerInch)
        assert(m.getPose().h() == Angle(90.0, AngleUnit.DEGREES).toRadians())
        m.reset()
        val tr = TestRunner()
        tr.movements = listOf(DrivePowerCoefficients.of(1.0), DrivePowerCoefficients.of(-1.0))
        tr.run(m.driver)
        println(m.getPose())
        println(tr.calculatePosition())
        assert(tr.calculatePosition() / specs.ticksPerInch == m.getPose())
    }
}