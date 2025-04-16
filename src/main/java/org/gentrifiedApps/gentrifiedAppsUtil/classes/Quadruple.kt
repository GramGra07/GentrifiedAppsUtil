package org.gentrifiedApps.gentrifiedAppsUtil.classes

data class Quadruple<T>(
    val first: T,
    val second: T,
    val third: T,
    val fourth: T
) {

    override fun toString(): String {
        return "Quadruple(first=$first, second=$second, third=$third, fourth=$fourth)"
    }
}
