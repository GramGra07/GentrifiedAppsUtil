package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes

/**
 * @param pitch Angle in degrees
 * @param roll Angle in degrees of tilt fwd/back (when camera faces up, roll is positive)
 * @param yaw Angle in degrees
 */
data class RotationVector(
    var pitch: Double,
    var roll: Double,
    var yaw: Double
) {
    init {
        pitch = Math.toRadians(pitch)
        roll = Math.toRadians(roll)
        yaw = Math.toRadians(yaw)
    }
    constructor() : this(0.0, 0.0, 0.0)
}