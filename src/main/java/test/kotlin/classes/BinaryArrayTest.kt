package test.kotlin.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.BinaryArray
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.Color
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals

class BinaryArrayTest {

    @Test
    fun testReadBase() {
        val binaryArray = BinaryArray(3)
        binaryArray[0] = 1.0
        binaryArray[1] = 0.0
        binaryArray[2] = 1.0
        val resultBase2 = binaryArray.readBase(2)
        assertEquals(3, resultBase2.size())
        assertEquals(1.0, resultBase2[0])
        assertEquals(0.0, resultBase2[1])
        assertEquals(1.0, resultBase2[2])
    }

    @Test
    fun testReadBase2() {
        val binaryArray = BinaryArray(3)
        binaryArray[0] = 1.0
        binaryArray[1] = 0.0
        binaryArray[2] = 1.0
        val resultBase2 = binaryArray.readBase2()
        assertEquals(3, resultBase2.size())
        assertEquals(1.0, resultBase2[0])
        assertEquals(0.0, resultBase2[1])
        assertEquals(1.0, resultBase2[2])
    }

    @Test
    fun testToBase() {
        val binaryArray = BinaryArray(3)
        binaryArray[0] = 1.0
        binaryArray[1] = 0.0
        binaryArray[2] = 1.0

        val resultBase10 = binaryArray.toBase(10)
        assertEquals(1, resultBase10.size())
        assertEquals(5.0, resultBase10[0]) // 101 in binary is 5 in decimal

        val resultBase2 = binaryArray.toBase(2)
        assertEquals(3, resultBase2.size())
        assertEquals(1.0, resultBase2[0])
        assertEquals(0.0, resultBase2[1])
        assertEquals(1.0, resultBase2[2])
    }

    @Test
    fun testToHexadecimal() {
        val binaryArray = BinaryArray(4)
        binaryArray[0] = 1.0
        binaryArray[1] = 1.0
        binaryArray[2] = 0.0
        binaryArray[3] = 1.0

        val resultHex = binaryArray.toHexadecimal()
        System.out.println(resultHex.toString())
        assertEquals(1, resultHex.size())
        assertEquals(13.0, resultHex[0]) // 'd' in hexadecimal is 13 in decimal
    }

    @Test
    fun testConstructorAndGetSet() {
        val arr = BinaryArray(3)
        arr[0] = 1.0
        arr[1] = 0.0
        arr[2] = 1.0

        assertEquals(3, arr.size())
        assertEquals(1.0, arr[0])
        assertEquals(0.0, arr[1])
        assertEquals(1.0, arr[2])
    }

    @Test
    fun testVarargConstructor() {
        val arr = BinaryArray(1.0, 0.0, 1.0)
        assertEquals(3, arr.size())
        assertEquals(1.0, arr[0])
        assertEquals(0.0, arr[1])
        assertEquals(1.0, arr[2])
    }

    @Test
    fun testColorConversion() {
        val red = BinaryArray(0.0, 1.0)
        val blue = BinaryArray(1.0, 0.0)
        val yellow = BinaryArray(1.0, 1.0)
        val none = BinaryArray(0.0, 0.0)

        assertEquals(Color.RED, red.toColor())
        assertEquals(Color.BLUE, blue.toColor())
        assertEquals(Color.YELLOW, yellow.toColor())
        assertEquals(Color.NONE, none.toColor())
    }

    @Test
    fun testColor1Conversion() {
        val red = BinaryArray(0.0)
        val blue = BinaryArray(1.0)

        assertEquals(Color.RED, red.toColor1())
        assertEquals(Color.BLUE, blue.toColor1())
    }

    @Test
    fun testAllianceConversion() {
        val red = BinaryArray(0.0)
        val blue = BinaryArray(1.0)
        val fallback = BinaryArray(2.0)

        assertEquals(Alliance.RED, red.toAlliance())
        assertEquals(Alliance.BLUE, blue.toAlliance())
        assertEquals(Alliance.RED, fallback.toAlliance()) // fallback to RED
    }

    @Test
    fun testToBase10Conversion() {
        val binary = BinaryArray(1.0, 0.0, 1.0) // binary 101 = 5
        val result = binary.toBase10()
        assertEquals(1, result.size())
        assertEquals(5.0, result[0])
    }

    @Test
    fun testToBase2Conversion() {
        val binary = BinaryArray(1.0, 0.0, 1.0)
        val result = binary.toBase2()
        assertEquals(binary.size(), result.size())
        assertArrayEquals(DoubleArray(3) { binary[it] }, DoubleArray(3) { result[it] })
    }

    @Test
    fun testToOctalConversion() {
        val binary = BinaryArray(1.0, 1.0, 1.0) // 111 = 7
        val result = binary.toOctal()
        assertEquals(1, result.size())
        assertEquals(7.0, result[0])
    }

    @Test
    fun testToHexConversion() {
        val binary = BinaryArray(1.0, 1.0, 0.0, 1.0) // 1101 = 13
        val result = binary.toHexadecimal()
        assertEquals(1, result.size())
        assertEquals(13.0, result[0])
    }

    @Test
    fun testReadBase10() {
        val fromBase10 = BinaryArray(5.0)
        val result = fromBase10.readBase10() // Interpreted as "5" and re-encoded
        assertEquals(1, result.size())
        assertEquals(5.0, result[0])
    }

    @Test
    fun testToString() {
        val arr = BinaryArray(1.0, 0.0, 1.0)
        assertEquals("1.0, 0.0, 1.0", arr.toString())
    }

    @Test
    fun testZeroConversion() {
        val zero = BinaryArray(0.0, 0.0, 0.0)
        val result = zero.toBase10()
        assertEquals(1, result.size())
        assertEquals(0.0, result[0])
    }
}