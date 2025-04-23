package org.gentrifiedApps.gentrifiedAppsUtil.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotor

class MotorExtensions {
    companion object {
        @JvmStatic fun resetMotor(motor: DcMotor): DcMotor {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            return motor
        }

        @JvmStatic fun resetMotor(motor: DcMotor, mode: DcMotor.RunMode): DcMotor {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = mode
            return motor
        }
    }
}