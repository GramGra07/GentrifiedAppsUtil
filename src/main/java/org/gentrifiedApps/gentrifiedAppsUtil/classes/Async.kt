package org.gentrifiedApps.gentrifiedAppsUtil.classes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Async {
    companion object {
        /**
         * Runs a block of code asynchronously.
         * @param action The block of code to run.
         */
        fun runAsync(action: suspend () -> Unit): Job {
            return GlobalScope.launch {
                action()
            }
        }

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
    }
}