package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.enums.Color

enum class Alliance {
    RED, BLUE;

    fun toChar(): String {

        return when (this) {
            RED -> "R"
            BLUE -> "B"
        }
    }

    /**
     * Converts an alliance to a binary array
     * @return BinaryArray with a length of 1, 0 if red, 1 if blue
     */
    fun toBinary(): BinaryArray {
        return when (this) {
            RED -> BinaryArray(1).apply {
                this[0] = 0.0
            }

            BLUE -> BinaryArray(1).apply {
                this[0] = 1.0
            }
        }
    }

    /**
     * Converts an alliance to a color
     * @return Color with the color of the alliance
     */
    fun toColor(): Color {
        return when (this) {
            RED -> Color.RED
            BLUE -> Color.BLUE
        }
    }

    /**
     * Converts an alliance to a binary array
     * @return BinaryArray with a length of 2, 01 if red, 10 if blue
     */
    fun toBinary2(): BinaryArray {
        return when (this) {
            RED -> BinaryArray(2).apply {
                this[0] = 0.0
                this[1] = 1.0
            }

            BLUE -> BinaryArray(2).apply {
                this[0] = 1.0
                this[1] = 0.0
            }
        }
    }
}
