package org.gentrifiedApps.gentrifiedAppsUtil.dev

/**
 * Runs a function and times how long it takes to run it.
 * @param function the function to run
 * @return a pair of the result of the function and the time it took to run it (seconds)
 */
fun timedFunction(function: Runnable): Pair<Any, Double> {
    val start = System.currentTimeMillis()
    val result = function.run()
    val end = System.currentTimeMillis()
    return Pair(result, (end - start).toDouble() / 1000)
}