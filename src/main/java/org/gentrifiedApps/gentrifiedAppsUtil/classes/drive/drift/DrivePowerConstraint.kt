package org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift

data class DrivePowerConstraint(
    val frontLeft: Double,
    val frontRight: Double,
    val backLeft: Double,
    val backRight: Double
){
    companion object {
        fun zeros(): DrivePowerConstraint {
            return DrivePowerConstraint(0.0, 0.0, 0.0, 0.0)
        }
    }

    operator fun times(b: Double): DrivePowerConstraint {
        return DrivePowerConstraint(
            this.frontLeft * b,
            this.frontRight * b,
            this.backLeft * b,
            this.backRight * b
        )
    }

    operator fun div(b: Double): DrivePowerConstraint {
        return DrivePowerConstraint(
            this.frontLeft / b,
            this.frontRight / b,
            this.backLeft / b,
            this.backRight / b
        )
    }
}
