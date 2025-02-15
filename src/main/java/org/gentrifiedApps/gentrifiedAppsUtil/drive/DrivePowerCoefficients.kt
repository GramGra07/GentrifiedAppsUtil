package org.gentrifiedApps.gentrifiedAppsUtil.drive

/**
 * A class to hold the coefficients for field centric driving.
 * @param frontLeft The coefficient for the front left motor.
 * @param frontRight The coefficient for the front right motor.
 * @param backLeft The coefficient for the back left motor.
 * @param backRight The coefficient for the back right motor.
 */
data class DrivePowerCoefficients(
    val frontLeft: Double,
    val frontRight: Double,
    val backLeft: Double,
    val backRight: Double
){
    companion object{
        fun zeros(): DrivePowerCoefficients{
            return DrivePowerCoefficients(0.0,0.0,0.0,0.0)
        }
    }
    fun notZero():Boolean{
        return this != zeros()
    }
}
