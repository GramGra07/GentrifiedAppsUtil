package org.gentrifiedApps.gentrifiedAppsUtil.classes.callbacks

/**
 * A class that allows for a timeout to be set and checked for
 * @param allowedTime the time in seconds that the timeout is allowed to run for in seconds
 * @param breakCondition a lambda that returns a boolean that will break the timeout if true
 */
class Timeout @JvmOverloads constructor(
    private var allowedTime: Double,
    private var breakCondition: () -> Boolean? = { false }
) {
    companion object {
        @JvmStatic
        fun newTimeout(allowedTime: Double, breakCondition: () -> Boolean? = { false }): Timeout {
            return Timeout(allowedTime, breakCondition)
        }
    }

    private var startTime = 0.0
    private var currentTime = 0.0
    private var elapsedTime = 0.0
    private var isTimedOut = false

    init {
        require(allowedTime > 0) { "Timeout time must be greater than 0" }
        require(allowedTime < 30) { "Timeout time must be less than 30" }
        start()
    }

    fun reset() {
        isTimedOut = false
        startTime = System.currentTimeMillis().toDouble() / 1000
        currentTime = 0.0
        elapsedTime = 0.0
    }

    /**
     * Starts the timeout
     * This is already called in class creation
     */
    fun start() {
        startTime = System.currentTimeMillis().toDouble() / 1000
    }

    /**
     * Updates the timeout
     * @return true if the timeout has been reached or break condition is true
     */
    fun update(): Boolean {
        currentTime = System.currentTimeMillis().toDouble() / 1000
        elapsedTime = currentTime - startTime
        if ((elapsedTime > allowedTime || breakCondition() == true) && !isTimedOut) {
            isTimedOut = true
        }
        return isTimedOut
    }

    fun isTimedOut(): Boolean {
        return isTimedOut
    }
}