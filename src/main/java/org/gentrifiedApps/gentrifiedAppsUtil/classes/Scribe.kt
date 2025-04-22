package org.gentrifiedApps.gentrifiedAppsUtil.classes

import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

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

    fun startLogger(opMode: LinearOpMode) {
        startLogger(opMode.javaClass.simpleName)
    }

    companion object {
        private const val DEFAULT_TAG = "Scribe"

        @JvmStatic
        var instance: Scribe = Scribe(DEFAULT_TAG)

        internal fun create(tagger: String) {
            this.instance = Scribe(DEFAULT_TAG + tagger)
        }

        internal fun reset() {
            this.instance = Scribe(DEFAULT_TAG)
        }

        internal fun isRunningTests(): Boolean {
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
