package org.gentrifiedApps.gentrifiedAppsUtil.classes.odometer

import android.os.Environment
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.EncoderSpecs
import java.io.File
import java.io.FileWriter
import java.io.IOException

class OdometerFileManager {
    val odometerFileName = "odometer.txt"
    val odometerConfig = "odometerConfig.txt"

    lateinit var odometerFile: File
    lateinit var odometerConfigFile: File
    var odoFileExists = false
    var odoConfigFileExists = false

    init {
        getFiles()
        createIfNotExists()
    }

    fun getFiles() {
        val filePath =
            "${Environment.getExternalStorageDirectory().absolutePath}/FIRST/odometer/$odometerFileName"
        try {
            val file = File(filePath)
            this.odometerFile = file
            if (file.exists()) {
                this.odoFileExists = true
            }
            Scribe.instance.setSet("Odometer").logDebug("File path: $filePath")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val configPath =
            "${Environment.getExternalStorageDirectory().absolutePath}/FIRST/odometer/$odometerConfig"
        try {
            val file = File(configPath)
            this.odometerConfigFile = file
            if (file.exists()) {
                this.odoConfigFileExists = true
            }
            Scribe.instance.setSet("Odometer").logDebug("File path: $configPath")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun createIfNotExists() {
        val filePath =
            "${Environment.getExternalStorageDirectory().absolutePath}/FIRST/odometer/$odometerFileName"
        try {
            val file = File(filePath)
            file.parentFile.mkdirs()
//            if (file.exists()) {
//                file.delete()
//            }
            file.createNewFile()
            this.odometerFile = file
            this.odoFileExists = true
//            writeOdometryData(0.0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun writeOdometryData(total: Double) {
        if (odometerFile.exists()) {
            try {
                val fileWriter = FileWriter(odometerFile, false) // Overwrite mode
                fileWriter.write(total.toString())
                fileWriter.close()
                Scribe.instance.setSet("Odometer").logDebug("Data written to file: $total")
            } catch (e: IOException) {
                e.printStackTrace()
                Scribe.instance.setSet("Odometer").logError("Failed to write to file: ${e.message}")
            }
        }
    }

    fun writeConfigData(config: String) {
        if (odometerConfigFile.exists()) {
            try {
                val fileWriter = FileWriter(odometerConfigFile, false) // Overwrite mode
                fileWriter.write(config)
                fileWriter.close()
                Scribe.instance.setSet("Odometer").logDebug("Data written to file: $config")
            } catch (e: IOException) {
                e.printStackTrace()
                Scribe.instance.setSet("Odometer").logError("Failed to write to file: ${e.message}")
            }
        }
    }

    fun readOdometryData(): Double {
        if (odometerFile.exists()) {
            try {
                val lines = odometerFile.readLines().filter { it.isNotEmpty() }
                if (lines.isNotEmpty()) {
                    return lines[0].toDouble()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Scribe.instance.setSet("Odometer")
                    .logError("Failed to read odometry data: ${e.message}")
            }
        }
        return 0.0
    }

    fun readConfigData(): EncoderSpecs? {
        if (odometerConfigFile.exists()) {
            try {
                val lines = odometerConfigFile.readLines()
                if (lines.size > 1) {
                    Scribe.instance.setSet("Odometer")
                        .logDebug("Error reading config file, too many lines")
                } else if (lines.isEmpty()) {
                    Scribe.instance.setSet("Odometer")
                        .logDebug("Error reading config file, empty file")
                    return EncoderSpecs(0.0)
                } else {
                    val line = lines[0]
                    return EncoderSpecs(line.toDouble())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }
}