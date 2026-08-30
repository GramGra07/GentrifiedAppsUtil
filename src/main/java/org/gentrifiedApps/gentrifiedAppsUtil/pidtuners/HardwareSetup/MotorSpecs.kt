package com.dacodingbeast.pidtuners.HardwareSetup

/**
 * Holds the necessary specs needed for this simulation, all which can be found on the vendor's website
 * @param rpm Theoretical rpm
 * @param stallTorque The motors stall torque in Nm
 * @param motorGearRatio Any gear conversions that need to be considered
 * Gear ratio is in the form of a fraction: (Motor gear teeth) / (Arm Gear Teeth)
 */
open class MotorSpecs(
    var rpm: Double,
    var stallTorque: Double,
    var motorGearRatio: Double = 1.0,
    var encoderTicksPerRotation: Double,
) {
    constructor(
        rpm: Double,
        stallTorque: Double,
        encoderTicksPerRotation: Double,
    ) : this(rpm, stallTorque, 1.0, encoderTicksPerRotation)

    init {
        if (motorGearRatio == 0.0) {
            throw IllegalArgumentException("Gear Ratio cannot be 0")
        } else if (motorGearRatio < 0.0) {
            throw IllegalArgumentException("Gear Ratio cannot be negative")
        }
        if (encoderTicksPerRotation == 0.0) {
            throw IllegalArgumentException("Encoder Ticks per Rotation cannot be 0")
        } else if (encoderTicksPerRotation < 0.0) {
            throw IllegalArgumentException("Encoder Ticks per Rotation cannot be negative")
        }
        if (rpm == 0.0) {
            throw IllegalArgumentException("RPM cannot be 0")
        } else if (rpm < 0.0) {
            throw IllegalArgumentException("RPM cannot be negative")
        }
    }
}