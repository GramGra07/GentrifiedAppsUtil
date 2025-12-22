package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.TestWrapperSuite
import kotlin.test.Test

class RobotSimTest {
    @Test
    fun genericBuiltTest() {
        val rso: RobotSimulatorOpMode = RobotSimulatorOpMode()
        rso.runOpMode()
        TestWrapperSuite.assert(rso.leftDrive != null, "Left Drive non null")
        TestWrapperSuite.assert(rso.rightDrive != null, "Right Drive non null")
        TestWrapperSuite.assert(rso.leftDrive.getPower() == 1.0, "left power == 1")
        TestWrapperSuite.assert(rso.rightDrive.getPower() == 0.0, "right power == 0")
    }
}