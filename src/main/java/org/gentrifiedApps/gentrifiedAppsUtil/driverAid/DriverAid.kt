package org.gentrifiedApps.gentrifiedAppsUtil.driverAid

import org.jetbrains.annotations.TestOnly
import java.util.function.BooleanSupplier

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

    /**
     * A function to get the current driver aid function
     * Do not use this unless this is a test case
     * @return The current driver aid function
     * @see DAFunc
     */
    @TestOnly
    fun getDAFunc() = daFunc

    fun setDriverAidFunction(func: DAFunc<T>) {
        func.runInit()
    }
    init {
    }
    // auto run on start
//    fun runInit() {
//        daFunc?.runInit()
//    }

    fun update() {
        daFunc?.runALot()
    }

    fun idle(iEnum: T): DAFunc<T> {
        return DAFunc(this, iEnum).apply { this.runInit() }
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
        private val runConstant: Runnable,
        private val isEnded: BooleanSupplier?,
        /**
         * This can be used for things that need to be set to 0 when the function is initialized
         */
        private val resetOnInit: Runnable?
    ) {
        constructor(driverAid: DriverAid<T>, state: T, funcs: Runnable,runConstant: Runnable, isEnded: BooleanSupplier) : this(driverAid, state, funcs, runConstant, isEnded, null)
        constructor(driverAid: DriverAid<T>, state: T, funcs: Runnable, runConstant: Runnable) : this(driverAid, state, funcs, runConstant, null,null)

        /**
         * IDLE constructor
         */
        constructor(driverAid: DriverAid<T>, state: T) : this(driverAid, state, Runnable {}, Runnable {}, null, null)
        fun runInit() {
            driverAid.daState = state
            funcs.run()
            driverAid.daFunc = this
            resetOnInit?.run()
        }

        fun runALot() {
            runConstant.run()
        }

        fun isEnded(): Boolean {
            return isEnded?.asBoolean == true
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