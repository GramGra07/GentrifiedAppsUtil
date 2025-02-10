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

    fun addSensor(sensor: Sensor) {
        array[sensor.name] = sensor
        array[sensor.name]!!.initializeSensor()
    }

    fun readAllLoopSaving() {
        array.forEach { sensor -> sensor.sensor().readLoopSaving() }
    }

    fun allTelemetry(telemetry: Telemetry) {
        array.forEach { sensor -> telemetry(telemetry, sensor.key) }
    }

    fun telemetry(telemetry: Telemetry, name: String) {
        telemetry.addData(name, array[name]!!.lastRead())
    }

    fun read(name: String) {
        array[name]!!.lastRead()
    }

    fun autoLoop(loops: Int) {
        array.forEach { sensor ->
            if (loops % sensor.sensor().period == 0) {
                sensor.sensor().readLoopSaving()
            }
        }
    }
}