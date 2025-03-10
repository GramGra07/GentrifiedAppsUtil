package org.gentrifiedApps.gentrifiedAppsUtil.classes

/**
 * A class that allows for a timeout to be set and checked for
 * @param allowedTime the time in seconds that the timeout is allowed to run for in seconds
 * @param breakCondition a lambda that returns a boolean that will break the timeout if true
 */
class Timeout(private var allowedTime:Double, private var breakCondition : () -> Boolean?) {
    private var startTime = 0.0
    private var currentTime = 0.0
    private var elapsedTime = 0.0
    private var isTimedOut = false
init {
    start()
}
    /**
     * Starts the timeout
     * This is already called in class creation
     */
    fun start() {
        startTime = System.currentTimeMillis().toDouble()/1000
    }

    /**
     * Updates the timeout
     * @return true if the timeout has been reached or break condition is true
     */
    fun update() :Boolean{
        currentTime = System.currentTimeMillis().toDouble()/1000
        elapsedTime = currentTime - startTime
        if ((elapsedTime > allowedTime || breakCondition() == true)&&!isTimedOut) {
            isTimedOut = true
            Scribe.instance.logDebug("Timeout reached")
        }
        return isTimedOut
    }

    fun isTimedOut(): Boolean {
        return isTimedOut
    }
}