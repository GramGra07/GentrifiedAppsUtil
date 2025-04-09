package org.gentrifiedApps.gentrifiedAppsUtil.motion.controllers

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pid.PIDCoefficients

class PIDController(kP: Double, kI: Double, kD: Double) : PIDFController(kP, kI, kD, 0.0) {
    constructor(pidCoefficients: PIDCoefficients) : this(
        pidCoefficients.kP,
        pidCoefficients.kI,
        pidCoefficients.kD
    )

    fun setPID(kP: Double, kI: Double, kD: Double) {
        setPIDF(kP, kI, kD, 0.0)
    }
}