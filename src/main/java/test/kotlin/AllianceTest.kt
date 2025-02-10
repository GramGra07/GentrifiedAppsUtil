package test.kotlin

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.BinaryArray
import org.gentrifiedApps.velocityvision.enums.Color
import org.junit.Test

class AllianceTest {
    @Test
    fun testAlliance() {
        assert(Alliance.RED.toChar() == "R")
        assert(Alliance.BLUE.toChar() == "B")
        assert(Alliance.RED.toBinary().toColor1() == Color.RED)
        assert(Alliance.BLUE.toBinary().toColor1() == Color.BLUE)
        assert(Alliance.RED.toColor() == Color.RED)
        assert(Alliance.BLUE.toColor() == Color.BLUE)
        assert(Alliance.RED.toBinary2().toColor() == Color.RED)
        assert(Alliance.BLUE.toBinary2().toColor() == Color.BLUE)
    }
    @Test
    fun testBinaryArrayToAlliance(){
        val binaryArray = BinaryArray(1)
        binaryArray[0] = 0.0
        assert(binaryArray.toAlliance() == Alliance.RED)
        binaryArray[0] = 1.0
        assert(binaryArray.toAlliance() == Alliance.BLUE)
        binaryArray[0] = 2.0
        assert(binaryArray.toAlliance() == Alliance.RED)
    }
}