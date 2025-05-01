package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.Color
import kotlin.math.pow

/**
 * A class to represent a binary array of 1 and 0
 * @param size The size of the array
 * @constructor Creates a binary array of the given size
 * @see Color
 */
class BinaryArray(size: Int) {
    constructor(vararg elements: Double) : this(elements.size) {
        for (i in elements.indices) {
            array[i] = elements[i]
        }
    }

    private val array: DoubleArray = DoubleArray(size)

    // Add methods to manipulate the array as needed
    operator fun get(index: Int): Double {
        return array[index]
    }

    operator fun set(index: Int, value: Double) {
        array[index] = value
    }

    fun size(): Int {
        return array.size
    }

    override fun toString(): String {
        return array.joinToString(", ")
    }

    /**
     * Converts the binary array to a color with the size of 1
     */
    fun toColor1(): Color {
        return if (this[0] == 0.0) {
            Color.RED
        } else {
            Color.BLUE
        }
    }

    /**
     * Converts the binary array to a color
     * @return The color represented by the binary array
     * @see Color
     */
    fun toColor(): Color {
        return if (this[0] == 0.0 && this[1] == 1.0) {
            Color.RED
        } else if (this[0] == 1.0 && this[1] == 0.0) {
            Color.BLUE
        } else if (this[0] == 1.0 && this[1] == 1.0) {
            Color.YELLOW
        } else {
            Color.NONE
        }
    }

    /**
     * Converts a binary array to an alliance
     * @return Alliance with the alliance of the binary array, red if 0, blue if 1, red if anything else
     */
    fun toAlliance(): Alliance {
        return when (this[0]) {
            0.0 -> Alliance.RED
            1.0 -> Alliance.BLUE
            else -> Alliance.RED
        }
    }

    fun readBase(base: Int): BinaryArray {
        var value = 0
        for (i in 0 until size()) {
            value += (this[i] * base.toDouble().pow(i.toDouble())).toInt()
        }

        return BinaryArray(*value.toString(base).map { it.toString().toDouble() }.toDoubleArray())
    }

    fun readBase10(): BinaryArray {
        return readBase(10)
    }

    fun readBase2(): BinaryArray {
        return readBase(2)
    }

    fun readOctal(): BinaryArray {
        return readBase(8)
    }

    fun readHexadecimal(): BinaryArray {
        return readBase(16)
    }

    fun toBase(base: Int): BinaryArray {
        // Always read the binary array as base 2 first
        var value = 0
        for (i in 0 until size()) {
            value += (this[i] * 2.0.pow(size() - 1 - i)).toInt()
        }

        // Convert that base-10 value to the target base
        val result = mutableListOf<Double>()
        if (value == 0) result.add(0.0)
        while (value > 0) {
            result.add((value % base).toDouble())
            value /= base
        }

        return BinaryArray(*result.reversed().toDoubleArray())
    }


    fun toBase10(): BinaryArray {
        return toBase(10)
    }

    fun toBase2(): BinaryArray {
        return toBase(2)
    }

    fun toOctal(): BinaryArray {
        return toBase(8)
    }

    fun toHexadecimal(): BinaryArray {
        return toBase(16)
    }
}