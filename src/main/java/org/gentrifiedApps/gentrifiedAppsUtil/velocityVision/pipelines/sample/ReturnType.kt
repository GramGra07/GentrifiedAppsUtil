package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.sample

enum class ReturnType {
    ANGLE,
    CENTER,
    COLOR;

    companion object {
        @JvmStatic
        fun all(): List<ReturnType> {
            return listOf(ANGLE, CENTER, COLOR)
        }
    }
}