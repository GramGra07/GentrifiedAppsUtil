package org.gentrifiedApps.gentrifiedAppsUtil.sensorArray

data class SensorReturn(
    var enc: Double = 0.0,
    var dist: Double = 0.0,
    var color: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0),
    var touch: Boolean = false
){
    fun blank(): SensorReturn {
        return SensorReturn()
    }
}
