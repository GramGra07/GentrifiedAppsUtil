package org.gentrifiedApps.gentrifiedAppsUtil.sensorArray

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

/**
 * Sensor class to represent a sensor
 * @param name The name of the sensor
 * @param type The type of the sensor
 * @param initializer The initializer for the sensor
 * @param period The period to update the sensor
 * @see SensorType
 * @see SensorArray
 */
class Sensor(var name:String, private var type: SensorType, private var initializer: Runnable, var period: Int) {
    private lateinit var enc: DcMotorEx
    private lateinit var dist: DistanceSensor
    private lateinit var color: ColorSensor
    private lateinit var touch: TouchSensor

    private var lastRead = SensorReturn().blank()

    fun lastRead(): Any {
        return when (type) {
            SensorType.ENC -> lastRead.enc
            SensorType.DIST -> lastRead.dist
            SensorType.COLOR -> lastRead.color
            SensorType.TOUCH -> lastRead.touch
        }
    }

    fun readLoopSaving() {
        val temp = read()
        lastRead = temp
    }
    private fun read(): SensorReturn {
        return when (type) {

            SensorType.ENC -> {
                lastRead.enc = enc.currentPosition.toDouble()
                lastRead
            }

            SensorType.DIST -> {
                lastRead.dist = dist.getDistance(DistanceUnit.INCH)
                lastRead
            }

            SensorType.COLOR -> {
                lastRead.color = doubleArrayOf(color.red().toDouble(), color.green().toDouble(), color.blue().toDouble())
                lastRead
            }

            SensorType.TOUCH -> {
                lastRead.touch = touch.isPressed
                lastRead
            }
        }

    }

    fun initializeSensor() {
        initializer.run()
    }
}