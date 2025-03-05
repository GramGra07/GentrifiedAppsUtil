package org.gentrifiedApps.gentrifiedAppsUtil.classes

import android.util.Log

class Scribe private constructor(val tagger: String){
    fun logData(data: Any){
        log(Log.INFO,data)
    }
    fun logError(data: Any){
        log(Log.ERROR,data)
    }
    fun logWarning(data: Any){
        log(Log.WARN,data)
    }
    fun logDebug(data: Any){
        log(Log.DEBUG,data)
    }
    fun startLogger(name: String){
        logData("Starting OpMode: $name")
    }
    companion object{
        @JvmStatic
        val defaultTagger = "Scribe"
        @JvmStatic
        var instance : Scribe = Scribe(defaultTagger)
        @JvmStatic
        fun create(tagger: String){
            this.instance = Scribe(tagger)
        }
        @JvmStatic
        fun reset(){
            this.instance = Scribe(defaultTagger)
        }
    }

    private fun log(priority: Int, data: Any){
        Log.println(priority,tagger,data.toString())
    }
}
