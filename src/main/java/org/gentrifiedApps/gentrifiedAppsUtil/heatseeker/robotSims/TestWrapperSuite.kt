package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robotSims

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.OutputFormatter

internal class TestWrapperSuite {
    companion object {
        fun assert(condition: Boolean, message: String) {
            var tag = "Assertion Passed"
            if (!condition) {
                tag = "Assertion Failed"
            }
            OutputFormatter.Companion.instance.sendData(tag, message)
            assert(condition)
        }
    }
}