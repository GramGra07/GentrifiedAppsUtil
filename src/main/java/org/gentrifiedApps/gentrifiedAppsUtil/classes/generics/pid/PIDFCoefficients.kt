package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pid

open class PIDFCoefficients(
    open var kP: Double,
    open var kI: Double,
    open var kD: Double,
    var kF: Double
) {
    constructor(kP: Double, kI: Double, kD: Double) : this(kP, kI, kD, 0.0)
}

data class PIDCoefficients(
    override var kP: Double,
    override var kI: Double,
    override var kD: Double
) : PIDFCoefficients(kP, kI, kD, 0.0)