package com.dacodingbeast.pidtuners.utilities.MathFunctions

class MotorMath {
    companion object{
        fun rads_sec(rpm: Double, gearRatio: Double):Double = ((rpm*(Math.PI*2))/60)*gearRatio
        fun output_ticks_per_rev(ticksPerRev:Double,gearRatio: Double): Double = ticksPerRev/gearRatio
        fun ticks_per_in_slide(output_ticks_per_rev:Double, slideRad: Double):Double = output_ticks_per_rev/(2*Math.PI*slideRad)
        fun ticks_per_sec(rpm:Double, gearRatio: Double,ticksPerRev: Double):Double = (((rpm*(Math.PI*2))/60)*gearRatio)*(ticksPerRev/(2*Math.PI))
        fun stall_torque(bare_stall_torque:Double, gearRatio: Double):Double = bare_stall_torque/gearRatio
        fun spool_rad_meters(slideRad: Double):Double = slideRad*0.0254
        fun max_linear_force(stallTorque:Double, spool_rad_meters:Double):Double = stallTorque/spool_rad_meters
        fun ticks_per_meter(ticksPerIn: Double):Double = ticksPerIn/0.0254
    }

}