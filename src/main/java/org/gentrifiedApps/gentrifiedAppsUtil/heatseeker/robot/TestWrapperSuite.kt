package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot

internal class TestWrapperSuite {
    companion object {
        fun assert(condition: Boolean, message: String) {
            var tag = "Assertion Passed"
            if (!condition) {
                tag = "Assertion Failed"
            }
            OutputFormatter.instance.sendData(tag, message)
            assert(condition)
        }
    }
}
