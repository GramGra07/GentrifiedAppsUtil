package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robotSims

import kotlin.test.Test

class HeatseekerRobotSimTest {
    @Test
    fun genericBuiltTest() {
        val rso: HeatseekerSimulatorOpMode = HeatseekerSimulatorOpMode()
        rso.runOpMode()
        TestWrapperSuite.assert(rso.driver.fl != null, "LeftFront non null")
        TestWrapperSuite.assert(rso.driver.fl.power == 1.0, "LeftFront power")
        TestWrapperSuite.assert(rso.driver.fr != null, "RightFront non null")
        TestWrapperSuite.assert(rso.driver.fr.power == 1.0, "RightFront power")

    }
}