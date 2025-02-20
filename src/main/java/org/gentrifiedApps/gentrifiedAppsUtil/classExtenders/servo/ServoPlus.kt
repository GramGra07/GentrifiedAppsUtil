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
        return this.manufacturer
    }

    override fun getDeviceName(): String {
        return this.deviceName
    }

    override fun getConnectionInfo(): String {
        return this.connectionInfo
    }

    override fun getVersion(): Int {
        return this.version
    }

    override fun resetDeviceConfigurationForOpMode() {
        return this.resetDeviceConfigurationForOpMode()
    }

    override fun close() {
        return this.close()
    }

    override fun getController(): ServoController {
        return this.controller
    }

    override fun getPortNumber(): Int {
        return this.portNumber
    }

    override fun setDirection(direction: Servo.Direction?) {
        return this.setDirection(direction)
    }

    override fun getDirection(): Servo.Direction {
        return this.direction
    }

    override fun getPosition(): Double {
        return this.position
    }

    override fun scaleRange(min: Double, max: Double) {
        return this.scaleRange(min, max)
    }
}