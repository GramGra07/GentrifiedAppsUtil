package org.gentrifiedApps.gentrifiedAppsUtil.classes.vision

import org.firstinspires.ftc.vision.opencv.ColorSpace
import org.gentrifiedApps.gentrifiedAppsUtil.classes.BinaryArray
import org.opencv.core.Scalar

enum class Color {
    RED, BLUE, YELLOW, NONE;

    fun cSpace(colorSpace: ColorSpace): DualScalarPair {
        return when (colorSpace) {
            ColorSpace.YCrCb -> when (this) {
                Color.RED -> ScalarPair(Scalar(0.0, 150.0, 85.0), Scalar(255.0, 200.0, 135.0))
                Color.BLUE -> ScalarPair(Scalar(0.0, 100.0, 140.0), Scalar(255.0, 130.0, 180.0))
                Color.YELLOW -> ScalarPair(Scalar(0.0, 140.0, 100.0), Scalar(255.0, 170.0, 130.0))
                Color.NONE -> ScalarPair(Scalar(0.0, 0.0, 0.0), Scalar(0.0, 0.0, 0.0))
            }

            ColorSpace.HSV -> when (this) {
                Color.RED -> DualScalarPair(
                    Scalar(0.0, 100.0, 100.0), Scalar(10.0, 255.0, 255.0),
                    Scalar(170.0, 100.0, 100.0), Scalar(180.0, 255.0, 255.0)
                )
                Color.BLUE -> ScalarPair(Scalar(100.0, 150.0, 0.0), Scalar(130.0, 255.0, 255.0))
                Color.YELLOW -> ScalarPair(Scalar(20.0, 100.0, 100.0), Scalar(30.0, 255.0, 255.0))
                Color.NONE -> ScalarPair(Scalar(0.0, 0.0, 0.0), Scalar(0.0, 0.0, 0.0))
            }

            ColorSpace.RGB -> when (this) {
                Color.RED -> ScalarPair(Scalar(150.0, 0.0, 0.0), Scalar(255.0, 80.0, 80.0))
                Color.BLUE -> ScalarPair(Scalar(0.0, 0.0, 150.0), Scalar(80.0, 80.0, 255.0))
                Color.YELLOW -> ScalarPair(Scalar(150.0, 150.0, 0.0), Scalar(255.0, 255.0, 100.0))
                Color.NONE -> ScalarPair(Scalar(0.0, 0.0, 0.0), Scalar(0.0, 0.0, 0.0))
            }
        }
    }
    companion object {
        fun fromString(color: String): Color {
            return when (color.lowercase()) {
                "red" -> RED
                "blue" -> BLUE
                "yellow" -> YELLOW
                else -> NONE
            }
        }
        fun toString(color: Color): String {
            return when (color) {
                RED -> "red"
                BLUE -> "blue"
                YELLOW -> "yellow"
                NONE -> "none"
            }
        }
        fun fromBinaryArray(array: BinaryArray): Color {
            return when {
                array[0] == 0.0 && array[1] == 1.0 -> RED
                array[0] == 1.0 && array[1] == 0.0 -> BLUE
                array[0] == 1.0 && array[1] == 1.0 -> YELLOW
                else -> NONE
            }
        }
        fun toBinaryArray(color: Color): BinaryArray {
            return when (color) {
                RED -> BinaryArray(2).apply { this[0] = 0.0; this[1] = 1.0 }
                BLUE -> BinaryArray(2).apply { this[0] = 1.0; this[1] = 0.0 }
                YELLOW -> BinaryArray(2).apply { this[0] = 1.0; this[1] = 1.0 }
                NONE -> BinaryArray(2).apply { this[0] = 0.0; this[1] = 0.0 }
            }
        }

    }
}