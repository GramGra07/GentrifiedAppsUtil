package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.RobotSimulatorOpMode
import org.junit.jupiter.api.Test

class RobotSimTest {
    @Test
    fun genericBuiltTest() {
        val rso: RobotSimulatorOpMode = RobotSimulatorOpMode()
        rso.runOpMode()
        assert(rso.leftDrive != null)
        assert(rso.rightDrive != null)
    }
}