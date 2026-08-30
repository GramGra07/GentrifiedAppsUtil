package com.dacodingbeast.pidtuners.Opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.dacodingbeast.pidtuners.HardwareSetup.PivotSpecs
import com.dacodingbeast.pidtuners.HardwareSetup.SlideSpecs
import com.dacodingbeast.pidtuners.PID.PIDFCoefs
import com.dacodingbeast.pidtuners.PID.PivotPIDFCoefs
import com.dacodingbeast.pidtuners.utilities.Coupling
import com.dacodingbeast.pidtuners.utilities.MathFunctions.MotorMath
import com.dacodingbeast.pidtuners.utilities.MathFunctions.PhysicsMath
import com.dacodingbeast.pidtuners.utilities.Solver

class PIDCalculator(var pivotSpecs: PivotSpecs?, var slideSpecs: SlideSpecs?, var OS: Double = 0.5, var TS:Double = 0.5) : LinearOpMode() {
    var coupled = false
    var pivotSpecsOnly = false
    var slideSpecsOnly = false
    var slideParams: PIDFCoefs? = null
    var pivotParams_low: PIDFCoefs? = null
    var pivotParams: PivotPIDFCoefs? = null
    init{
        coupled = pivotSpecs != null && slideSpecs != null
        pivotSpecsOnly = pivotSpecs != null && slideSpecs == null
        slideSpecsOnly = pivotSpecs == null && slideSpecs != null
    }

    override fun runOpMode() {
        telemetry.addLine("Initializing Physics-Based PID...")
        telemetry.update()

        waitForStart()

        telemetry.clear()
        telemetry.addLine("Running physics-based PID control...")
        telemetry.update()

            if (coupled){
                val coupling: Coupling = Coupling(slideSpecs!!.max_slide_ticks, slideSpecs!!.min_slide_ticks, PivotPIDFCoefs.zeros())
                pivotParams = Solver.find_pidf_params_pivot_coupled(PhysicsMath.I(pivotSpecs!!.armMass, pivotSpecs!!.armLengthCollapsed, pivotSpecs!!.manipMass),PhysicsMath.kG(pivotSpecs!!.armMass,pivotSpecs!!.armLengthCollapsed,pivotSpecs!!.manipMass),MotorMath.stall_torque(pivotSpecs!!.stallTorque, pivotSpecs!!.gearRatio),MotorMath.stall_torque(slideSpecs!!.stallTorque, slideSpecs!!.gearRatio),pivotSpecs!!.armMass,pivotSpecs!!.manipMass,MotorMath.ticks_per_meter(MotorMath.ticks_per_in_slide(MotorMath.output_ticks_per_rev(slideSpecs!!.ticks_per_rev,slideSpecs!!.gearRatio),slideSpecs!!.spool_rad)),pivotSpecs!!.armLengthCollapsed,coupling)
                slideParams = Solver.find_pidf_params_slide(slideSpecs!!.manipMass, MotorMath.ticks_per_meter(MotorMath.ticks_per_in_slide(MotorMath.output_ticks_per_rev(slideSpecs!!.ticks_per_rev,slideSpecs!!.gearRatio),slideSpecs!!.spool_rad)), MotorMath.max_linear_force(MotorMath.stall_torque(slideSpecs!!.stallTorque, slideSpecs!!.gearRatio), MotorMath.spool_rad_meters(slideSpecs!!.spool_rad)))
            }else if (pivotSpecsOnly){
                pivotParams_low = Solver.find_pidf_params_pivot_no_coupling(PhysicsMath.I(pivotSpecs!!.armMass, pivotSpecs!!.armLengthCollapsed, pivotSpecs!!.manipMass),PhysicsMath.kG(pivotSpecs!!.armMass,pivotSpecs!!.armLengthCollapsed,pivotSpecs!!.manipMass),MotorMath.stall_torque(pivotSpecs!!.stallTorque, pivotSpecs!!.gearRatio))
            }
            if (slideSpecsOnly){
                slideParams = Solver.find_pidf_params_slide(slideSpecs!!.manipMass, MotorMath.ticks_per_meter(MotorMath.ticks_per_in_slide(MotorMath.output_ticks_per_rev(slideSpecs!!.ticks_per_rev,slideSpecs!!.gearRatio),slideSpecs!!.spool_rad)), MotorMath.max_linear_force(MotorMath.stall_torque(slideSpecs!!.stallTorque, slideSpecs!!.gearRatio), MotorMath.spool_rad_meters(slideSpecs!!.spool_rad)))
            }
        while(opModeIsActive()) {

            telemetry.addLine("OpMode ended, PID Params as follows:")
            if (slideSpecsOnly || coupled) telemetry.addLine("Slide Params: ${slideParams?.output()}")
            if (pivotSpecsOnly) telemetry.addLine("Pivot Params: ${pivotParams_low?.output()}")
            if (coupled) telemetry.addLine("Pivot Params: ${pivotParams?.output()}")
            telemetry.update()
        }
    }
}
