package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics

import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions

class ComparablePair<T>(var first: T, var second: T) :
    Comparable<ComparablePair<T>> where T : Comparable<T> {
    override fun compareTo(other: ComparablePair<T>): Int {
        val firstComparison = first.compareTo(other.first)
        return if (firstComparison != 0) {
            firstComparison
        } else {
            second.compareTo(other.second)
        }
    }

    fun isDifferent(): Boolean {
        return first != second
    }

    fun isSame(): Boolean {
        return first == second
    }

    fun inTolerance(tolerance: T): Boolean {
        require(first is Number && second is Number && tolerance is Number) { "Tolerance can only be applied to numeric types" }
        val firstValue = first as Number
        val secondValue = second as Number
        val toleranceValue = tolerance as Number
        return MathFunctions.inTolerance(
            firstValue.toDouble(),
            secondValue.toDouble(),
            toleranceValue.toDouble()
        )
    }

    override fun toString(): String {
        return "ComparablePair(first=$first, second=$second)"
    }
}

class TimeMachinePair<T>(var previous: T, var current: T) :
    Comparable<TimeMachinePair<T>> where T : Comparable<T> {
    override fun compareTo(other: TimeMachinePair<T>): Int {
        val previousComparison = previous.compareTo(other.previous)
        return if (previousComparison != 0) {
            previousComparison
        } else {
            current.compareTo(other.current)
        }
    }

    fun isDifferent(): Boolean {
        return previous != current
    }

    fun isSame(): Boolean {
        return previous == current
    }

    fun inTolerance(tolerance: T): Boolean {
        require(previous is Number && current is Number && tolerance is Number) { "Tolerance can only be applied to numeric types" }
        val firstValue = previous as Number
        val secondValue = current as Number
        val toleranceValue = tolerance as Number
        return MathFunctions.inTolerance(
            firstValue.toDouble(),
            secondValue.toDouble(),
            tolerance.toDouble()
        )
    }

    override fun toString(): String {
        return "TimeMachinePair(previous=$previous, current=$current)"
    }
}