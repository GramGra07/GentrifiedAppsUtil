package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles.controllers

class PIDController(kP: Double, kI: Double, kD: Double) : PIDFController(kP, kI, kD, 0.0) {

    fun setPID(kP: Double, kI: Double, kD: Double) {
        setPIDF(kP, kI, kD, 0.0)
    }
}