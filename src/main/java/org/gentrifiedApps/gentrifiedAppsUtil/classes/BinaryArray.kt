package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.Color

/**
 * A class to represent a binary array of 1 and 0
 * @param size The size of the array
 * @constructor Creates a binary array of the given size
 * @see Color
 */
class BinaryArray(size: Int) {
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
}