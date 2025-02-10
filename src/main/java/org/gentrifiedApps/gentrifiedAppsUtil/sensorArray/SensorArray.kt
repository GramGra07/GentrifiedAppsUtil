package org.gentrifiedApps.gentrifiedAppsUtil.sensorArray

import org.firstinspires.ftc.robotcore.external.Telemetry


//ex

//val sensorArray: SensorArray = SensorArray()
//sensorArray.addSensor( Sensor("pitchEncoder",SensorType.ENC, { pitchMotor = hardwareMap.get(DcMotorEx::class.java, "pitchMotor") }, 1))
/**
 * A way to represent the array of sensors, as well as reading them all at once
 * @see Sensor
 * @see SensorType
 */
class SensorArray {
    private var array: HashMap<String, Sensor> = HashMap()

    private fun Map.Entry<String,Sensor>.sensor():Sensor {
        return this.value
    }

    /**
     * Adds a sensor to the array
     * @param sensor The sensor to add
     * @see Sensor
     */
    fun addSensor(sensor: Sensor) {
        array[sensor.name] = sensor
        array[sensor.name]!!.initializeSensor()
    }

    /**
     * Reads all sensors, should be used every loop at the beginning
     */
    fun readAllLoopSaving() {
        array.forEach { sensor -> sensor.sensor().readLoopSaving() }
    }

    /**
     * Telemetry for all sensors
     * @param telemetry The telemetry to use
     */

    fun allTelemetry(telemetry: Telemetry) {
        array.forEach { sensor -> telemetry(telemetry, sensor.key) }
    }

    /**
     * Telemetry for an individual sensor
     * @param telemetry The telemetry to use
     * @param name The name of the sensor
     */
    fun telemetry(telemetry: Telemetry, name: String) {
        telemetry.addData(name, array[name]!!.lastRead())
    }

    /**
     * Reads an individual sensor
     * @param name The name of the sensor
     */
    fun read(name: String) {
        array[name]!!.lastRead()
    }

    /**
     * Reads all sensors at its periodic
     * @param loops The amount of loops, loopTimeController.loops
     */
    fun autoLoop(loops: Int) {
        array.forEach { sensor ->
            if (loops % sensor.sensor().period == 0) {
                sensor.sensor().readLoopSaving()
            }
        }
    }
}