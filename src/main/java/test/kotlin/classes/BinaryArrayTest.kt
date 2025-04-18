package test.kotlin.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.BinaryArray
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.Color
import org.junit.Test

class BinaryArrayTest {
    @Test
    fun testBinaryArray() {
        val binaryArray = BinaryArray(2)
        binaryArray[0] = 0.0
        binaryArray[1] = 1.0
        assert(binaryArray.toColor() == Color.RED)
    }
    @Test
    fun testCreation(){
        val binaryArray = BinaryArray(2)
        assert(binaryArray.size() == 2)
        assert(binaryArray[0] == 0.0)
        assert(binaryArray[1] == 0.0)
        binaryArray[0] = 1.0
        assert(binaryArray[0] == 1.0)
    }
}