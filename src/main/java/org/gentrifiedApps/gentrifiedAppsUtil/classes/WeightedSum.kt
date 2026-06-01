package org.gentrifiedApps.gentrifiedAppsUtil.classes

class WeightedSum(var weights: Array<Double>) {

    fun of(values: Array<Double>): Double {
        require(weights.size == values.size)
        return values.mapIndexed { index, value -> value * weights[index] }.sum()
    }
}