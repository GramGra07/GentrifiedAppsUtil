package org.gentrifiedApps.gentrifiedAppsUtil.driverAid

import org.gentrifiedApps.gentrifiedAppsUtil.driverAid.DriverAid.DAFunc
import org.jetbrains.annotations.TestOnly

/**
 * A class to help with driver aid actions, automatically and continuously running functions
 * based on the state of the robot and what is sent.
 * @param T The enum class to use
 * @param enumClass The enum class to use
 * @property daState The current state of the driver aid
 * @see DAFunc
 */
class DriverAid<T : Enum<T>>(enumClass: Class<T>) {
    var daState: Enum<T>? = null
    private var daFunc: DAFunc<T>? = null

    @TestOnly
    fun getDAFunc() = daFunc

    init {
        require(enumClass.enumConstants?.any { it.name == "IDLE" } == true) {
            "Enum class must have an IDLE constant"
        }
    }

    fun runInit() {
        daFunc?.runInit()
    }

    fun update() {
        daFunc?.runALot()
    }

    /**
     * A function to run functions based on the state of the robot.
     * @param driverAid The driver aid class to use
     * @param state The state to run the functions in
     * @param funcs The functions to run only once
     * @param runConstant The functions to run constantly
     * @param isEnded A lambda to check if the functions are done
     * @param resetOnInit A lambda to run when the functions are initialized
     * @constructor Creates a new driver aid function
     */
    class DAFunc<T : Enum<T>>(
        private val driverAid: DriverAid<T>,
        val state: T,
        private val funcs: Runnable,
        private val runConstant: Runnable?,
        private val isEnded: java.util.function.BooleanSupplier,
        private val resetOnInit: Runnable?
    ) {
        fun runInit() {
            driverAid.daState = state
            funcs.run()
            driverAid.daFunc = this
            resetOnInit?.run()
        }

        fun runALot() {
            runConstant?.run()
        }

        fun isEnded(): Boolean {
            return isEnded.asBoolean
        }
    }

    //TODO show action implementation
//    class DAActions(
//        private val funcs: List<Runnable>,
//    ) : Action {
//        override fun run(packet: TelemetryPacket): Boolean {
//            funcs.forEach(Runnable::run)
//            return false
//        }
//    }
//
//    fun daAction(funcs: List<Runnable>): Action {
//        return DAActions(funcs)
//    }
}