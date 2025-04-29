package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses

class Distance @JvmOverloads constructor(
    val value: Double,
    val distanceUnit: DistanceUnit = DistanceUnit.INCH
) {
    // convert from any to any others
    fun convertTo(to: DistanceUnit): Distance {
        val from = distanceUnit
        if (from == to) {
            return this
        }
        val valueInMM = when (from) {
            DistanceUnit.MM -> value
            DistanceUnit.CM -> value * 10
            DistanceUnit.M -> value * 1000
            DistanceUnit.INCH -> value * 25.4
            DistanceUnit.FEET -> value * 304.8
            DistanceUnit.YARD -> value * 914.4
        }
        val convertedValue = when (to) {
            DistanceUnit.MM -> valueInMM
            DistanceUnit.CM -> valueInMM / 10
            DistanceUnit.M -> valueInMM / 1000
            DistanceUnit.INCH -> valueInMM / 25.4
            DistanceUnit.FEET -> valueInMM / 304.8
            DistanceUnit.YARD -> valueInMM / 914.4
        }
        return Distance(convertedValue, to)
    }

    fun convertToMM(): Distance {
        return convertTo(DistanceUnit.MM)
    }
}