package org.gentrifiedApps.gentrifiedAppsUtil.decode

import com.qualcomm.robotcore.eventloop.opmode.OpMode.blackboard
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.DecodeColor

enum class PatternType() {
    PPG, PGP, GPP, NONE;

    fun store() {
        blackboard.put("Motif", this)
    }

    fun getFromMemory(): PatternType {
        return blackboard.get("Motif") as PatternType? ?: NONE
    }

    override fun toString(): String {
        return this.name
    }

    fun middle(): DecodeColor {
        return when (this) {
            PPG -> DecodeColor.PURPLE
            PGP -> DecodeColor.GREEN
            GPP -> DecodeColor.PURPLE
            NONE -> DecodeColor.PURPLE // Default case, should not happen
        }
    }

    fun first(): DecodeColor {
        return when (this) {
            PPG -> DecodeColor.PURPLE
            PGP -> DecodeColor.PURPLE
            GPP -> DecodeColor.GREEN
            NONE -> DecodeColor.PURPLE // Default case, should not happen
        }
    }

    fun last(): DecodeColor {
        return when (this) {
            PPG -> DecodeColor.GREEN
            PGP -> DecodeColor.PURPLE
            GPP -> DecodeColor.PURPLE
            NONE -> DecodeColor.PURPLE // Default case, should not happen
        }
    }

    fun toColor(index: Int): DecodeColor {
        return when (index) {
            0 -> DecodeColor.PURPLE
            1 -> DecodeColor.GREEN
            else -> DecodeColor.PURPLE // Default case, should not happen
        }
    }


}