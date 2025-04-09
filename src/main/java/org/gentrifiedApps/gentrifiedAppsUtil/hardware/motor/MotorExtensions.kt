package org.gentrifiedApps.gentrifiedAppsUtil.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotor

class MotorExtensions {
    companion object{
        fun resetMotor(motor: DcMotor){
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
        fun resetMotor(motor: DcMotor, mode: DcMotor.RunMode){
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = mode
        }
    }
}