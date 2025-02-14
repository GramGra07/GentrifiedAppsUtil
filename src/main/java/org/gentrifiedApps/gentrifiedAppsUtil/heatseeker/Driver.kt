package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients


enum class Drive_Type {
    TANK, MECANUM
}

class Driver(hwMap: HardwareMap) {
    val driveType = Drive_Type.TANK
    private var fl: DcMotor = hwMap.get(DcMotor::class.java, "fl")
    private var fr: DcMotor = hwMap.get(DcMotor::class.java, "fr")
    private var bl: DcMotor = hwMap.get(DcMotor::class.java, "bl")
    private var br: DcMotor = hwMap.get(DcMotor::class.java, "br")

    fun setWheelPower(powerCoefficients: DrivePowerCoefficients) {
        fl.power = powerCoefficients.frontLeft
        fr.power = powerCoefficients.frontRight
        bl.power = powerCoefficients.backLeft
        br.power = powerCoefficients.backRight
    }
}