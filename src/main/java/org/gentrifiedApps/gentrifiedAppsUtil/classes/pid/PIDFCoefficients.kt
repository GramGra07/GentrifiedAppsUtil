package org.gentrifiedApps.gentrifiedAppsUtil.classes.pid

open class PIDFCoefficients (open var kP: Double, open var  kI: Double, open var  kD: Double, var  kF: Double)

data class PIDCoefficients(override var kP: Double, override var kI: Double, override var kD: Double): PIDFCoefficients(kP, kI, kD, 0.0)