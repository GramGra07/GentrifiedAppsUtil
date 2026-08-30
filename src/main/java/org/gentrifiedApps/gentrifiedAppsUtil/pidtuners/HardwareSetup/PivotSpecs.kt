package com.dacodingbeast.pidtuners.HardwareSetup

class PivotSpecs(var rpm_:Double, var bare_stall_torque:Double, var ticks_per_rev:Double, var armMass:Double, var armLengthCollapsed: Double, var manipMass:Double, var gearRatio: Double): MotorSpecs(rpm_,bare_stall_torque, ticks_per_rev)