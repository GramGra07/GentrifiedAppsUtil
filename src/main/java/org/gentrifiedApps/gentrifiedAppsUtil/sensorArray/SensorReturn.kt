package org.gentrifiedApps.gentrifiedAppsUtil.sensorArray

data class SensorReturn @JvmOverloads constructor(
    var enc: Double = 0.0,
    var dist: Double = 0.0,
    var color: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0),
    var touch: Boolean = false,
    var analogEncoder: Double = 0.0,
    var custom: Any = 0.0
) {
    /**
     * Returns a blank SensorReturn object
     */
    fun blank(): SensorReturn {
        return SensorReturn()
    }
}
