package com.dacodingbeast.pidtuners.utilities

import android.provider.ContactsContract
import com.dacodingbeast.pidtuners.PID.PIDFCoefs
import com.dacodingbeast.pidtuners.PID.PivotPIDFCoefs
import com.dacodingbeast.pidtuners.utilities.MathFunctions.PhysicsMath
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import kotlin.math.ln
import kotlin.math.sqrt

class Solver {
    companion object{
        val OS = 0.5
        val ST = 0.5
        fun find_pidf_params_pivot_no_coupling(i:Double, kG: Double, integrandFactor:Double = 100.0, OS: Double = 0.5, ST: Double = 0.5) : PIDFCoefs{
            val zeta = (- (ln(OS/100)))/sqrt(Math.PI*Math.PI+ln(OS/100)*ln(OS/100))
            val omega_n = 4/(zeta*ST)
            val kP = i*omega_n*omega_n
            val kI = kP/integrandFactor
            val kD = 2*zeta*omega_n*i
            val kF = kG/kP
            return PIDFCoefs(kP, kI, kD, kF)
        }
        fun find_pidf_params_pivot_coupled(i: Double, kG:Double, stallTorque: Double, integrandFactor: Double = 100.0, armMass:Double, manipMass:Double, ticksPerMeter: Double, length:Double, coupling: Coupling): PivotPIDFCoefs{
            val zeta = (- (ln(OS/100)))/sqrt(Math.PI*Math.PI+ln(OS/100)*ln(OS/100))
            val omega_n = 4/(zeta*ST)
            val L_max = length+ (coupling.gamma_dif()/ticksPerMeter)
            val I_max = PhysicsMath.I(armMass,L_max, manipMass)
            val kP = RangePair<Double>(i*omega_n*omega_n/stallTorque, I_max*omega_n*omega_n/stallTorque)
            val kI = RangePair<Double>((kP.low/integrandFactor)/stallTorque, (kP.high/integrandFactor)/stallTorque)
            val kD = RangePair<Double>(2*zeta*omega_n*i/stallTorque, 2*zeta*omega_n*I_max/stallTorque)
            val kG = RangePair<Double>(kG, armMass*(length/2)+manipMass*L_max * PhysicsMath.G)
            val kF = RangePair<Double>(kG.low/stallTorque, kG.high/stallTorque)
            return PivotPIDFCoefs(0.0,kP,0.0, kI,0.0, kD, 0.0,kF)
        }
        fun find_pidf_params_slide(massManip:Double,ticksPerMeter: Double,maxLinearForce:Double, integrandFactor: Double = 100.0): PIDFCoefs{
            val zeta = (- (ln(OS/100)))/sqrt(Math.PI*Math.PI+ln(OS/100)*ln(OS/100))
            val omega_n = 4/(zeta*ST)
            val conv = ticksPerMeter*maxLinearForce
            val kP = massManip*omega_n*omega_n/conv
            val kI = (kP/integrandFactor)/conv
            val kD = 2*zeta*omega_n*massManip/conv
            val max_G = massManip*PhysicsMath.G
            val kF = max_G/conv
            return PIDFCoefs(kP, kI, kD, kF)
        }
    }
}