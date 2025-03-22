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
    private lateinit var configFile: File
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

        val filePath2 =
            "${Environment.getExternalStorageDirectory().absolutePath}/FIRST/teleTracker/$name/${name}Config.txt"
        try {
            val file = File(filePath2)
            file.parentFile.mkdirs()
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            configFile = file
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun buildConfigFile(data: PreemptiveLog) {
        if (configFile.exists()) {
            FileWriter(configFile).use { writer ->
                writer.write(data.toString())
            }
        }else{
            Scribe.instance.setSet("OpModeTracker").logWarning("Config file does not exist")
            throw IOException("Config file does not exist")
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
class PreemptiveLogger(val name: String) {
    var startTime : Double = 0.0
    var loops : Int = 0
    var endTime : Double = 0.0
    var lps : Double = 0.0
    var elapsedTimeS : Double = 0.0
    fun start(){
        startTime = (System.currentTimeMillis()*0.001).toDouble()
    }
    fun update(telemetry: Telemetry){
        loops++
        elapsedTimeS = (System.currentTimeMillis()*0.001).toDouble()-startTime
        telemetry.addData("time elapsed",elapsedTimeS)
    }
    fun end(){
        endTime =( System.currentTimeMillis()*0.001).toDouble()
        elapsedTimeS = endTime-startTime
        lps = loops/(elapsedTimeS)
    }
    fun verify(ltc:LoopTimeController){
        if (!inTolerance(lps,ltc.lps,10.0)){
            Scribe.instance.setSet("OpModeTrackerPreemptive").logWarning("LPS is not in tolerance")
        }
        if (!inTolerance(loops.toDouble(),ltc.loops.toDouble(),100.0)){
            Scribe.instance.setSet("OpModeTrackerPreemptive").logWarning("Loops is not in tolerance")
        }
    }
    fun returnData(): PreemptiveLog {
        return PreemptiveLog(elapsedTimeS,loops,lps)
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
data class PreemptiveLog(var time:Double, var loops: Int, var lps: Double){
    constructor(input: String) : this(time = 0.0, loops = 0, lps = 0.0) {
        try {
            val data = input.split(",")
            time = data[0].toDouble()
            loops = data[1].toInt()
            lps = data[2].toDouble()
        }catch (e:Exception){
            Scribe.instance.setSet("OpModeTrackerPreemptive").logWarning("Error parsing preemptive log")
            throw e
        }
    }
    fun telemetry(telemetry: Telemetry){
        telemetry.addLine("Tested On")
        telemetry.addData("Time Elapsed",time)
        telemetry.addData("Loops",loops)
        telemetry.addData("LPS",lps)
    }
    init {
        verify()
    }
    fun verify(){
        if (!MathFunctions.inTolerance(lps,loops/time,50.0)){//obviously will pass
            Scribe.instance.setSet("OpModeTracker").logWarning("LPS is not in tolerance")
        }
    }
}