package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.servo

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoController

/**
 * A class to extend the Servo features with more functionality
 * @param hardwareMap The hardware map to use
 * @param name The name of the servo
 * @param degree The degree range of the servo (typically 180)
 */
class ServoPlus(hardwareMap: HardwareMap, name: String, degree: Double = 180.0) : Servo {
    constructor(hardwareMap: HardwareMap, name: String) : this(hardwareMap, name, 180.0)

    private val servo: Servo = hardwareMap.get(Servo::class.java, name)
    private val multiplier = 1.0 / degree

    /**
     * This allows you to automatically set the position of the servo in degrees
     * @param position The position to set the servo to in degrees
     */
    override fun setPosition(position: Double) {
        servo.position = position * multiplier
    }


    override fun getManufacturer(): HardwareDevice.Manufacturer {
        return servo.manufacturer
    }

    override fun getDeviceName(): String {
        return servo.deviceName
    }

    override fun getConnectionInfo(): String {
        return servo.connectionInfo
    }

    override fun getVersion(): Int {
        return servo.version
    }

    override fun resetDeviceConfigurationForOpMode() {
        return servo.resetDeviceConfigurationForOpMode()
    }

    override fun close() {
        return servo.close()
    }

    override fun getController(): ServoController {
        return servo.controller
    }

    override fun getPortNumber(): Int {
        return servo.portNumber
    }

    override fun setDirection(direction: Servo.Direction?) {
        return servo.setDirection(direction)
    }

    override fun getDirection(): Servo.Direction {
        return servo.direction
    }

    override fun getPosition(): Double {
        return servo.position
    }

    override fun scaleRange(min: Double, max: Double) {
        return servo.scaleRange(min, max)
    }
}