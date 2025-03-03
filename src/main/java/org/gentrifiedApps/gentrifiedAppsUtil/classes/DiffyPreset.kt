package org.gentrifiedApps.gentrifiedAppsUtil.classes

/**
 * A class to store the left and right values of a diffy drive, allows to be used with DiffyServo apply preset
 * @param left The left value
 * @param right The right value
 * @see org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.servo.DiffyServo.applyPreset
 */
data class DiffyPreset(val left: Double, val right: Double)