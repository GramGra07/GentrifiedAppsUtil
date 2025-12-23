package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robotSims

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.TestWrapperSuite
import kotlin.test.Test

class BasicRobotSimTest {
    @Test
    fun genericBuiltTest() {
        val rso: BasicRobotSimulatorOpMode = BasicRobotSimulatorOpMode()
        rso.runOpMode()
        TestWrapperSuite.Companion.assert(rso.leftDrive != null, "Left Drive non null")
        TestWrapperSuite.Companion.assert(rso.rightDrive != null, "Right Drive non null")
        TestWrapperSuite.Companion.assert(rso.leftDrive.getPower() == 1.0, "left power == 1")
        TestWrapperSuite.Companion.assert(rso.rightDrive.getPower() == 0.0, "right power == 0")
    }
}