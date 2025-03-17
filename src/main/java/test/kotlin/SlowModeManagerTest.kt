package test.kotlin

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.GamepadPlus
import org.gentrifiedApps.gentrifiedAppsUtil.classes.SlowMode
import org.gentrifiedApps.gentrifiedAppsUtil.classes.SlowModeDefaults
import org.gentrifiedApps.gentrifiedAppsUtil.classes.SlowModeManager
import org.gentrifiedApps.gentrifiedAppsUtil.classes.SlowModeMulti
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import org.junit.Assert.*
import org.junit.Test
import kotlin.apply

class SlowModeManagerTest {
    @Test
    fun testConstructorWithGamepadPlus() {
        val slowModeManager = SlowModeManager(GamepadPlus(Gamepad()))
        assertNotNull(slowModeManager)
        assertEquals(1, slowModeManager.slowModeDataList.size)
        assertEquals(SlowModeDefaults.NORMAL, slowModeManager.currentlyActive)
    }

    @Test
    fun testConstructorWithList() {
        val slowModeMulti = SlowModeMulti(SlowMode(2.0), Button.A)
        val list = listOf(Pair(SlowModeDefaults.NORMAL, slowModeMulti))
        val slowModeManager = SlowModeManager(list, GamepadPlus(Gamepad()))
        assertNotNull(slowModeManager)
        assertEquals(1, slowModeManager.slowModeDataList.size)
        assertEquals(SlowModeDefaults.NORMAL, slowModeManager.currentlyActive)
    }

    @Test
    fun testApplyValue() {
        val slowMode = SlowMode(2.0)
        val slowModeMulti = SlowModeMulti(slowMode, Button.A)
        val slowModeManager = SlowModeManager(hashMapOf(SlowModeDefaults.NORMAL to slowModeMulti), GamepadPlus(Gamepad()))

        slowModeMulti.slowMode = true
        val result = slowModeManager.apply(4.0)
        assertEquals(2.0, result, 0.0)
    }

    @Test
    fun testApplyDrivePowerCoefficients() {
        val slowMode = SlowMode(2.0)
        val slowModeMulti = SlowModeMulti(slowMode, Button.A)
        val slowModeManager = SlowModeManager(hashMapOf(SlowModeDefaults.NORMAL to slowModeMulti), GamepadPlus(Gamepad()))

        slowModeMulti.slowMode = true
        val drivePowerCoefficients = DrivePowerCoefficients(4.0, 4.0, 4.0, 4.0)
        val result = slowModeManager.apply(drivePowerCoefficients)
        assertEquals(DrivePowerCoefficients(2.0, 2.0, 2.0, 2.0), result)
    }
}
class SlowModeTest {

    @Test
    fun testConstructor() {
        val slowMode = SlowMode(2.0)
        assertNotNull(slowMode)
        assertEquals(2.0, slowMode.slowModeFactor, 0.0)
    }

    @Test
    fun testBasicConstructor() {
        val slowMode = SlowMode.basic()
        assertNotNull(slowMode)
        assertEquals(2.0, slowMode.slowModeFactor, 0.0)
    }

    @Test
    fun testOneConstructor() {
        val slowMode = SlowMode.one()
        assertNotNull(slowMode)
        assertEquals(1.0, slowMode.slowModeFactor, 0.0)
    }

    @Test
    fun testOfConstructor() {
        val slowMode = SlowMode.of(3.0)
        assertNotNull(slowMode)
        assertEquals(3.0, slowMode.slowModeFactor, 0.0)
    }
}
class SlowModeMultiTest {

    @Test
    fun testConstructorWithDeactiveButton() {
        val slowMode = SlowMode(2.0)
        val slowModeMulti = SlowModeMulti(slowMode, Button.A, Button.B)
        assertNotNull(slowModeMulti)
        assertEquals(slowMode, slowModeMulti.slowModeData)
        assertEquals(Button.A, slowModeMulti.activeButton)
        assertEquals(Button.B, slowModeMulti.deactiveButton)
    }

    @Test
    fun testConstructorWithoutDeactiveButton() {
        val slowMode = SlowMode(2.0)
        val slowModeMulti = SlowModeMulti(slowMode, Button.A)
        assertNotNull(slowModeMulti)
        assertEquals(slowMode, slowModeMulti.slowModeData)
        assertEquals(Button.A, slowModeMulti.activeButton)
        assertEquals(Button.A, slowModeMulti.deactiveButton)
    }

    @Test
    fun testBasicConstructor() {
        val slowModeMulti = SlowModeMulti.basic()
        assertNotNull(slowModeMulti)
        assertEquals(2.0, slowModeMulti.slowModeData.slowModeFactor, 0.0)
        assertEquals(Button.A, slowModeMulti.activeButton)
        assertEquals(Button.A, slowModeMulti.deactiveButton)
    }
}