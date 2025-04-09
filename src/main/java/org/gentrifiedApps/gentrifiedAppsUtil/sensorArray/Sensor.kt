package org.gentrifiedApps.gentrifiedAppsUtil.sensorArray

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.gentrifiedApps.gentrifiedAppsUtil.classes.analogEncoder.AnalogEncoder
import org.gentrifiedApps.gentrifiedAppsUtil.classes.analogEncoder.Operation
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.sensors.BeamBreakSensor

/**
 * A class to represent the data of a sensor
 * @param returnType The type of data the sensor returns
 * @param name The name of the sensor
 * @see Sensor
 */
data class SensorData(
    val returnType: Class<out Any>,
    val name: String
) {
    companion object {
        @JvmStatic
        fun distanceSensor(name: String): SensorData {
            return SensorData(Double::class.java, name)
        }

        @JvmStatic
        fun analogEncoder(name: String): SensorData {
            return SensorData(Double::class.java, name)
        }

        @JvmStatic
        fun colorSensor(name: String): SensorData {
            return SensorData(DoubleArray::class.java, name)
        }

        @JvmStatic
        fun touchSensor(name: String): SensorData {
            return SensorData(Boolean::class.java, name)
        }

        @JvmStatic
        fun encoder(name: String): SensorData {
            return SensorData(Int::class.java, name)
        }

        @JvmStatic
        fun beamBreak(name: String): SensorData {
            return touchSensor(name)
        }
    }
}

/**
 * A class to represent a sensor
 * @param init The initialization of the sensor
 * @param sensorData The data of the sensor
 * @param read The function to read the sensor
 * @param period The period to read the sensor
 * @see SensorData
 * @see Sensor
 */
open class Sensor(
    val init: Runnable,
    val sensorData: SensorData,
    val read: () -> Any,
    val period: Int
) {
    constructor(sensorData: SensorData, read: () -> Any, period: Int) : this(
        Runnable {},
        sensorData,
        read,
        period
    )

    constructor(sensorData: SensorData, read: () -> Any) : this(Runnable {}, sensorData, read, 1)
    constructor(init: Runnable, sensorData: SensorData, read: () -> Any) : this(
        init,
        sensorData,
        read,
        1
    )

    private var lastRead: Any? = null

    init {
        require(period >= 1, { "Period must be greater than or equal to 1" })
        init.run()
    }

    fun lastRead(): Any {
        return lastRead ?: 0
    }

    fun read(): Any {
        lastRead = read.invoke()
        return lastRead ?: 0
    }

    fun readLoopSaving() {
        read()
    }

    companion object {
        @JvmStatic
        fun distanceSensor(hw: HardwareMap, name: String): Sensor {
            lateinit var sensorDistance: DistanceSensor
            return Sensor(
                Runnable { sensorDistance = hw.get(DistanceSensor::class.java, name) },
                SensorData.distanceSensor(name)
            ) { sensorDistance.getDistance(DistanceUnit.INCH) }
        }

        @JvmStatic
        fun analogEncoder(hw: HardwareMap, name: String, operations: List<Operation>): Sensor {
            lateinit var sensorAnalogEncoder: AnalogEncoder
            return Sensor(
                Runnable { sensorAnalogEncoder = AnalogEncoder(hw, name, operations) },
                SensorData.analogEncoder(name)
            ) { sensorAnalogEncoder.getCurrentPosition() }
        }

        @JvmStatic
        fun analogEncoder(analogEncoder: AnalogEncoder): Sensor {
            return Sensor(
                SensorData.analogEncoder(analogEncoder.name)
            ) { analogEncoder.getCurrentPosition() }
        }

        @JvmStatic
        fun colorSensor(hw: HardwareMap, name: String): Sensor {
            lateinit var sensorColor: ColorSensor
            return Sensor(
                Runnable { sensorColor = hw.get(ColorSensor::class.java, name) },
                SensorData.colorSensor(name)
            ) {
                doubleArrayOf(
                    sensorColor.red().toDouble(),
                    sensorColor.green().toDouble(),
                    sensorColor.blue().toDouble()
                )
            }
        }

        @JvmStatic
        fun touchSensor(hw: HardwareMap, name: String): Sensor {
            lateinit var sensorTouch: TouchSensor
            return Sensor(
                Runnable { sensorTouch = hw.get(TouchSensor::class.java, name) },
                SensorData.touchSensor(name)
            ) { sensorTouch.isPressed }
        }

        @JvmStatic
        fun encoder(hw: HardwareMap, name: String): Sensor {
            lateinit var sensorEncoder: DcMotor
            return Sensor(
                Runnable { sensorEncoder = hw.get(DcMotor::class.java, name) },
                SensorData.encoder(name)
            ) { sensorEncoder.currentPosition }
        }

        @JvmStatic
        fun beamBreak(hw: HardwareMap, name: String): Sensor {
            lateinit var beamBreakSensor: BeamBreakSensor
            return Sensor(
                Runnable { beamBreakSensor = BeamBreakSensor(hw, name) },
                SensorData.beamBreak(name)
            ) { beamBreakSensor.isBroken() }
        }

        @JvmStatic
        fun beamBreak(beamBreakSensor: BeamBreakSensor): Sensor {
            return Sensor(
                SensorData.beamBreak(beamBreakSensor.name)
            ) { beamBreakSensor.isBroken() }
        }
    }
}
