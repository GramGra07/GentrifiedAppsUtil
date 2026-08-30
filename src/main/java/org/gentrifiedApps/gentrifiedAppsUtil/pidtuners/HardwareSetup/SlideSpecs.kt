package com.dacodingbeast.pidtuners.HardwareSetup

class SlideSpecs(var rpm_:Double, var bare_stall_torque:Double,var spool_rad:Double, var ticks_per_rev:Double, var armMass:Double, var manipMass:Double, var gearRatio: Double, val max_slide_ticks:Double, val min_slide_ticks: Double): MotorSpecs(rpm_,bare_stall_torque, ticks_per_rev)