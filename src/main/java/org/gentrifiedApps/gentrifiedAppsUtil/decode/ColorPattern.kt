package org.gentrifiedApps.gentrifiedAppsUtil.decode

import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.DecodeColor

data class ColorPattern(var index0: Int, var index1: Int, var index2: Int) {
    constructor(list: List<Int>) : this(list[0], list[1], list[2])
    constructor() : this(0, 0, 0)
    constructor(a: DecodeColor, b: DecodeColor, c: DecodeColor) : this(
        when (a) {
            DecodeColor.PURPLE -> 0
            DecodeColor.GREEN -> 1
        },
        when (b) {
            DecodeColor.PURPLE -> 0
            DecodeColor.GREEN -> 1
        },
        when (c) {
            DecodeColor.PURPLE -> 0
            DecodeColor.GREEN -> 1
        }
    )

    constructor(aTag: Int) : this(
        //TODO: make sure this is right
        //TODO add error checking and test cases
    )

    fun asList(): List<Int> {
        return listOf(index0, index1, index2)
    }

    override fun toString(): String {
        return "ColorPattern(index0=$index0, index1=$index1, index2=$index2)"
    }

    fun toStringColors(): String {
        return "ColorPattern(first=${toColor(index0)}, middle=${toColor(index1)}, last=${
            toColor(
                index2
            )
        })"
    }

    fun middle(): Int {
        return index1
    }

    fun first(): Int {
        return index0
    }

    fun last(): Int {
        return index2
    }

    fun toColor(index: Int): DecodeColor {
        return when (index) {
            0 -> DecodeColor.PURPLE
            1 -> DecodeColor.GREEN
            else -> DecodeColor.PURPLE // Default case, should not happen
        }
    }
}