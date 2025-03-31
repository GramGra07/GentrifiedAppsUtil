package org.gentrifiedApps.gentrifiedAppsUtil.motionProfiles

class PIDController(kP: Double, kI: Double, kD: Double) : PIDFController(kP, kI, kD, 0.0) {

    constructor(kP: Double, kI: Double, kD: Double, sp: Double, pv: Double) : 
        super(kP, kI, kD, 0.0, sp, pv)

    fun setPID(kP: Double, kI: Double, kD: Double) {
        setPIDF(kP, kI, kD, 0.0)
    }
}