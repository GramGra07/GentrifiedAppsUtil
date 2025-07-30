package org.gentrifiedApps.gentrifiedAppsUtil.classes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Async {
    companion object {
        /**
         * Runs a block of code asynchronously and waits for its result.
         * @param action The block of code to run.
         * @return The result of the block.
         */
        suspend fun <T> runAsyncWithResult(action: suspend () -> T): T {
            return withContext(Dispatchers.Default) {
                action()
            }
        }

        fun whenAction(bool: Boolean, action: () -> Unit) {
            if (bool) {
                action()
            }
        }

        /**
         * Note that these will block any other program running in parallel
         */

        fun runAsync(funcs: List<suspend () -> Unit>) {
            runBlocking(Dispatchers.Default) {
                val jobs = funcs.map { func ->
                    async {
                        try {
                            func()
                        } catch (e: Exception) {
                            println("Task failed: ${e.message}")
                        }
                    }
                }
                jobs.awaitAll() // Ensure all tasks complete
            }
        }

    }
}