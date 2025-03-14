package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa

object DetectionExtensions {
    fun List<DetectionBuilder>.first(): DetectionBuilder {
        return this[0]
    }

    fun List<DetectionBuilder>.second(): DetectionBuilder {
        return this[1]
    }

    fun List<DetectionBuilder>.last(): DetectionBuilder {
        return this[this.size - 1]
    }
}