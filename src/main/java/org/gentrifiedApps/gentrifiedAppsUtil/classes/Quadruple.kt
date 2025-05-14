package org.gentrifiedApps.gentrifiedAppsUtil.classes

import java.lang.Double.sum

data class Quadruple<T>(
    val first: T,
    val second: T,
    val third: T,
    val fourth: T
) {
    constructor(one: T) : this(one, one, one, one)

    fun all0(): Boolean {
        return sum(
            first as Double,
            sum(second as Double, sum(third as Double, fourth as Double))
        ) == 0.0
    }

    override fun toString(): String {
        return "Quadruple(first=$first, second=$second, third=$third, fourth=$fourth)"
    }
}
