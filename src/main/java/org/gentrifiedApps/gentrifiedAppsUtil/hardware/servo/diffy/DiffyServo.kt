package org.gentrifiedApps.gentrifiedAppsUtil.hardware.servo.diffy

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.servo.ServoPlus

/**
 * A class to give you easy control over two servos
 * @param hardwareMap The hardware map of the robot
 * @param leftName The name of the left servo
 * @param rightName The name of the right servo
 * @param leftDirection The direction of the left servo
 * @param rightDirection The direction of the right servo
 * @param idlePosition The idle position of the servos
 */
class DiffyServo(
    hardwareMap: HardwareMap,
    leftName: String,
    rightName: String,
    leftDirection: Servo.Direction,
    rightDirection: Servo.Direction,
    idlePosition: Double
) {
    constructor(hardwareMap: HardwareMap, leftName: String, rightName: String) : this(
        hardwareMap,
        leftName,
        rightName,
        Servo.Direction.FORWARD,
        Servo.Direction.FORWARD,
        0.0
    )

    constructor(
        hardwareMap: HardwareMap,
        leftName: String,
        rightName: String,
        leftDirection: Servo.Direction,
        rightDirection: Servo.Direction
    ) : this(hardwareMap, leftName, rightName, leftDirection, rightDirection, 0.0)

    constructor(
        hardwareMap: HardwareMap,
        leftName: String,
        rightName: String,
        idlePosition: Double
    ) : this(
        hardwareMap,
        leftName,
        rightName,
        Servo.Direction.FORWARD,
        Servo.Direction.FORWARD,
        idlePosition
    )

    private var leftServo: ServoPlus = ServoPlus(hardwareMap, leftName)
    private var rightServo: Servo = ServoPlus(hardwareMap, rightName)
    private var lPos: Double = 0.0
    private var rPos: Double = 0.0

    init {
        leftServo.direction = leftDirection
        rightServo.direction = rightDirection
        lPos = idlePosition
        rPos = idlePosition
        setPosition()
    }

    /**
     * Sets the position of the servos
     */
    fun setPosition() {
        leftServo.position = getlPos()
        rightServo.position = getrPos()
    }

    private fun getlPos(): Double {
        return Range.clip(lPos, -1.0, 1.0)
    }

    private fun getrPos(): Double {
        return Range.clip(rPos, -1.0, 1.0)
    }

    fun rotateDown(position: Double) {
        //same direction
        lPos -= position
        rPos -= position
    }

    fun rotateUp(position: Double) {
        //same direction
        lPos += position
        rPos += position
    }

    fun rotateLeft(position: Double) {
        //opposite direction
        lPos -= position
        rPos += position
    }

    fun rotateRight(position: Double) {
        //opposite direction
        lPos += position
        rPos -= position
    }

    /**
     * Uses a DiffyPreset to set the left and right positions
     * @param preset The preset to use
     * @see DiffyPreset
     */
    fun applyPreset(preset: DiffyPreset) {
        lPos = preset.left
        rPos = preset.right
    }
}