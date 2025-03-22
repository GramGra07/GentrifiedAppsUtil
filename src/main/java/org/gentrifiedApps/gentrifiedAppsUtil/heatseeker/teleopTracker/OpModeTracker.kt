package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.teleopTracker

import android.os.Environment
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.inTolerance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController
import java.io.File
import java.io.FileWriter
import java.io.IOException

class OpModeTracker(val name: String) {
    private lateinit var file: File
    fun init(){
        createFiles()
    }
    fun createFiles() {
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
    }
    fun writeToFile(data: MovementData) {
        if (file.exists()) {
            FileWriter(file, true).use { writer ->
                writer.write(data.toString() + "\n")
            }
        }else{
            Scribe.instance.setSet("OpModeTracker").logWarning("File does not exist")
            throw IOException("File does not exist")
        }
    }
}
data class MovementData(val x: Double, val y: Double, val rotation: Double){
    constructor(x: Float, y: Float, rotation: Float) : this(x.toDouble(), y.toDouble(), rotation.toDouble())
    override fun toString(): String{
        return "$x,$y,$rotation"
    }
    fun fromString(input: String): MovementData{
        val data = input.split(",")
        return MovementData(data[0].toDouble(),data[1].toDouble(),data[2].toDouble())
    }
}