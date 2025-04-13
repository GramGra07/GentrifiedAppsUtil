package org.gentrifiedApps.gentrifiedAppsUtil.teleopTracker

import android.os.Environment
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import java.io.File
import java.io.FileWriter
import java.io.IOException

class OpModeTracker(val name: String) {
    private lateinit var file: File
    fun init() {
        createFiles()
        Scribe.instance.startLogger("OpModeTracker")
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
            try {
                val fileWriter = FileWriter(file, true)
                fileWriter.append(data.toString()).append(" \n")
                fileWriter.close()
                Scribe.instance.setSet("OpModeTracker")
                    .logDebug("Data written to file: $data")
            } catch (e: IOException) {
                e.printStackTrace()
                Scribe.instance.setSet("OpModeTracker")
                    .logError("Failed to write to file: ${e.message}")
            }
            Scribe.instance.setSet("OpModeTracker")
                .logDebug("Data written to file: $data")
        } else {
            Scribe.instance.setSet("OpModeTracker").logWarning("File does not exist")
            throw IOException("File does not exist")
        }
    }
}

data class MovementData(val x: Double, val y: Double, val rotation: Double, val time: Double) {
    constructor(x: Float, y: Float, rotation: Float, time: Double) : this(
        x.toDouble(),
        y.toDouble(),
        rotation.toDouble(),
        time
    )

    override fun toString(): String {
        return "$x,$y,$rotation,$time"
    }

    fun fromString(input: String): MovementData {
        val data = input.split(",")
        return MovementData(
            data[0].toDouble(),
            data[1].toDouble(),
            data[2].toDouble(),
            data[3].toDouble()
        )
    }
}