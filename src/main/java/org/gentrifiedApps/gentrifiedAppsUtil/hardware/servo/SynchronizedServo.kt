package org.gentrifiedApps.gentrifiedAppsUtil.hardware.servo

import com.qualcomm.robotcore.hardware.HardwareMap

class SynchronizedServo @JvmOverloads constructor(
    var hw: HardwareMap,
    var name: String,
    private var axonServo: Boolean = false
) {
    private lateinit var servo1: ServoPlus
    private lateinit var servo2: ServoPlus

    private lateinit var servo1Axon: AxonServo
    private lateinit var servo2Axon: AxonServo

    init {
        when (axonServo) {
            true -> {
                servo1Axon = AxonServo(hw, name + '1')
                servo2Axon = AxonServo(hw, name + '2')
            }

            false -> {
                servo1 = ServoPlus(hw, name + '1')
                servo2 = ServoPlus(hw, name + '2')
            }
        }
    }

    fun setPose(pose: Double) {
        when (axonServo) {
            true -> {
                servo1Axon.setPosition(90 + pose)
                servo2Axon.setPosition(90 - pose)
            }

            false -> {
                servo1.position = (90 + pose)
                servo2.position = (90 - pose)
            }
        }
    }
}