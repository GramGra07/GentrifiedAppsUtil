package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.velocityvision.enums.Color

enum class Alliance {
    RED, BLUE;

    fun toChar(): String {

        return when (this) {
            RED -> "R"
            BLUE -> "B"
        }
    }
}

/**
 * Converts an alliance to a binary array
 * @return BinaryArray with a length of 1, 0 if red, 1 if blue
 */
fun Alliance.toBinary(): BinaryArray {
    return when (this) {
        Alliance.RED -> BinaryArray(1).apply {
            this[0] = 0.0
        }

        Alliance.BLUE -> BinaryArray(1).apply {
            this[0] = 1.0
        }
    }
}

/**
 * Converts an alliance to a color
 * @return Color with the color of the alliance
 */
fun Alliance.toColor(): Color {
    return when (this) {
        Alliance.RED -> Color.RED
        Alliance.BLUE -> Color.BLUE
    }
}
/**
 * Converts an alliance to a binary array
 * @return BinaryArray with a length of 2, 01 if red, 10 if blue
 */
fun Alliance.toBinary2(): BinaryArray {
    return when (this) {
        Alliance.RED -> BinaryArray(2).apply {
            this[0] = 0.0
            this[1] = 1.0
        }

        Alliance.BLUE -> BinaryArray(2).apply {
            this[0] = 1.0
            this[1] = 0.0
        }
    }
}

/**
 * Converts a binary array to an alliance
 * @return Alliance with the alliance of the binary array, red if 0, blue if 1, red if anything else
 */
fun BinaryArray.toAlliance(): Alliance {
    return when (this[0]) {
        0.0 -> Alliance.RED
        1.0 -> Alliance.BLUE
        else -> Alliance.RED
    }
}