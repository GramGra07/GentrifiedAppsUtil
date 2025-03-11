package org.gentrifiedApps.gentrifiedAppsUtil.classes.analogEncoder

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Represents an analog encoder with a list of operations to perform on the input voltage.
 *
 * @param name The name of the encoder.
 * @param startPosition The starting position of the encoder.
 * @param calculatorOperations The list of operations to perform on the input voltage.
 */
class AnalogEncoder(private val hwMap: HardwareMap,val name: String, private val startPosition:Double, private val calculatorOperations:List<Operation>) {
    constructor(hwMap:HardwareMap,name: String,calculatorOperations: List<Operation>) : this(hwMap,name,0.0,calculatorOperations)
    private lateinit var analogEncoder: AnalogInput
    private lateinit var calculator: AnalogEncoderCalculator
    init {
        analogEncoder = hwMap.get(AnalogInput::class.java, name)
        calculator = AnalogEncoderCalculator(calculatorOperations)
    }

    /**
     * Gets the current position of the encoder using the result of the operations.
     * @return The current position of the encoder.
     */
    fun getCurrentPosition(): Int {
        return calculator.runOperations(analogEncoder.voltage).toInt() - startPosition.toInt()
    }

    companion object{
        @JvmStatic
        fun axon(hwMap: HardwareMap,name: String):AnalogEncoder{
            return AnalogEncoder(hwMap,name, listOf(Operation(Operand.DIVIDE,3.3),Operation(Operand.MULTIPLY,360.0)))
        }
        @JvmStatic
        fun rev_potentiometer(hwMap: HardwareMap,name: String):AnalogEncoder{
            return AnalogEncoder(hwMap,name, listOf(Operation(Operand.MULTIPLY,81.8)))
        }
    }
}
/**
 * Represents a calculator that performs a list of operations on an input value.
 *
 * @param operations The list of operations in order to perform.
 */
class AnalogEncoderCalculator(private val operations: List<Operation>){
    init {
        require(operations.isNotEmpty()){"Operations cannot be empty"}
    }
    /**
     * Runs the operations on the input value.
     *
     * @param input The input value to perform the operations on.
     * @return The result after performing all the operations.
     */
    fun runOperations(input:Double):Double{
        var returnable = input
        operations.forEach {
            returnable = it.runOperation(returnable)
        }
        return returnable
    }
}
/**
 * Represents an operation to perform on an input value.
 *
 * @param operation The operation to perform.
 * @param value The value to perform the operation on.
 */
class Operation(private val operation: Operand, private val value: Double){
    /**
     * Runs the operation on the input value.
     *
     * @param input The input value to perform the operation on.
     * @return The result after performing the operation.
     */
    fun runOperation(input:Double):Double{
        val returnable = when(operation){
            Operand.MULTIPLY -> input * value
            Operand.DIVIDE -> input / value
            Operand.ADD -> input + value
            Operand.SUBTRACT -> input - value
        }
        return returnable
    }
}
enum class Operand{
    MULTIPLY,
    DIVIDE,
    ADD,
    SUBTRACT
}