package org.gentrifiedApps.gentrifiedAppsUtil.classes

import android.util.Log
import org.jetbrains.annotations.TestOnly

class Scribe private constructor(private val tagger: String) {
    private var set = ""

    /**
     * Sets the tag for the logger
     * ONLY FOR TESTING and debugging for GAU
     * @param tag The tag to set
     * @return The logger
     */
    internal fun setSet(tag: String): Scribe {
        set = "-$tag"
        return this
    }

    private fun removeTag() {
        set = ""
    }

    fun logData(data: Any) {
        log(Log.INFO, data)
    }

    fun logError(data: Any) {
        log(Log.ERROR, data)
    }

    fun logWarning(data: Any) {
        log(Log.WARN, data)
    }

    fun logDebug(data: Any) {
        log(Log.DEBUG, data)
    }

    fun startLogger(name: String) {
        setSet("OpMode")
        logData("Starting OpMode: $name")
    }

    companion object {
        @JvmStatic
        private val defaultTagger = "Scribe"

        @JvmStatic
        var instance: Scribe = Scribe(defaultTagger)

        @JvmStatic
        fun create(tagger: String) {
            this.instance = Scribe(defaultTagger + tagger)
        }

        @JvmStatic
        fun reset() {
            this.instance = Scribe(defaultTagger)
        }

        private fun isRunningTests(): Boolean {
            return System.getProperty("java.class.path").contains("junit")
        }
    }

    private fun log(priority: Int, data: Any) {
        if (!isRunningTests()) {
            Log.println(priority, tagger + set, data.toString())
        }
        removeTag()
    }
}
