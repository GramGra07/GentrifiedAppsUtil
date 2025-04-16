package org.gentrifiedApps.gentrifiedAppsUtil.dataStorage

import android.os.Environment
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.AngleUnit
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D
import java.io.File
import java.io.FileWriter
import java.io.IOException

internal class DataStore {
    private lateinit var file: File
    private val tag = "store"
    internal fun init() {
        createFiles()
    }

    internal fun createFiles() {
        val filePath =
            "${Environment.getExternalStorageDirectory().absolutePath}/FIRST/dataStore/$tag.txt"
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

    internal fun writeToFile() {
        val dataStore = DataStorage.getData()
        val alliance = dataStore.first
        val pose = dataStore.second.toPose2D() // everything in rad
        val data = "$alliance,${pose.x},${pose.y},${pose.h}"
        if (file.exists()) {
            try {
                val fileWriter = FileWriter(file, false)
                fileWriter.write(data.toString())
                fileWriter.close()
                Scribe.instance.setSet("DataStore")
                    .logDebug("Data written to file: $data")
            } catch (e: IOException) {
                e.printStackTrace()
                Scribe.instance.setSet("DataStore")
                    .logError("Failed to write to file: ${e.message}")
            }
            Scribe.instance.setSet("DataStore")
                .logDebug("Data written to file: $data")
        } else {
            Scribe.instance.setSet("DataStore").logWarning("File does not exist")
            throw IOException("File does not exist")
        }
    }

    internal fun readFromFile(): Pair<Alliance, Target2D> {
        val lines = file.readLines()
        for (line in lines) {
            if (line.isEmpty()) {
                lines.drop(lines.indexOf(line))
            }
            if (line.split(',').size != 4) {
                Scribe.instance.setSet("DataStore")
                    .logError("Line: ${lines.indexOf(line)} is not in the correct format, is instead: $line")
            }
        }
        if (lines.isEmpty()) {
            Scribe.instance.setSet("DataStore").logError("File is empty")
            return Pair(Alliance.RED, Target2D(0.0, 0.0, Angle(0.0, AngleUnit.RADIANS)))
        }
        val line = lines[0].split(',')
        return Pair(
            Alliance.fromString(line[0]),
            Target2D(
                line[1].toDouble(),
                line[2].toDouble(),
                Angle(
                    line[3].toDouble(), AngleUnit.RADIANS
                )
            )
        )
    }
}