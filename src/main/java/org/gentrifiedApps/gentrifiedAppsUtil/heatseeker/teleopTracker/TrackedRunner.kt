package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.teleopTracker

import android.os.Environment
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import java.io.File
import java.io.FileWriter
import java.io.IOException

class TrackedRunner(val name: String) {
    lateinit var file: File
    lateinit var configFile: File
    lateinit var lines: List<String>
    init {
        getFiles()
        readFile()
    }
    fun getFiles(){
        val filePath =
            "${Environment.getExternalStorageDirectory().absolutePath}/FIRST/teleTracker/$name/$name.txt"
        try {
            val file = File(filePath)
            file.parentFile.mkdirs()
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            this.file = file
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val configFilePath =
            "${Environment.getExternalStorageDirectory().absolutePath}/FIRST/teleTracker/$name/${name}Config.txt"
        try {
            val configFile = File(configFilePath)
            configFile.parentFile.mkdirs()
            if (configFile.exists()) {
                configFile.delete()
            }
            configFile.createNewFile()
            this.configFile = configFile
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun readConfigFile(): PreemptiveLog {
        return PreemptiveLog(configFile.readText())
    }
    fun readFile(){
        val lines = file.readLines()
        this.lines = verifyLines(lines)
    }
    fun verifyLines(input: List<String>): List<String> {
        for (line in input){
            if (line.isEmpty()){
                input.drop(input.indexOf(line))
            }
            if (line.split(',').size != 3){
                Scribe.instance.setSet("TrackedRunner").logError("Line: ${input.indexOf(line)} is not in the correct format, is instead: $line")
            }
        }
        return input
    }
    var currentIndex = 0
    fun iterate(): MovementData?{
        if (currentIndex >= lines.size){
            return null
        }
        val line = lines[currentIndex].split(',')
        currentIndex++
        return MovementData(line[0].toDouble(), line[1].toDouble(), line[2].toDouble())
    }
}