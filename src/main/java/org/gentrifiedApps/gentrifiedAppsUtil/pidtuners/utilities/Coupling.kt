package com.dacodingbeast.pidtuners.utilities

import com.dacodingbeast.pidtuners.PID.PivotPIDFCoefs

data class Coupling(var slide_max_ticks: Double, var slide_min_ticks: Double, var p_pidfCoefs: PivotPIDFCoefs){
    fun gamma_dif() = this.slide_max_ticks-this.slide_min_ticks
    fun scal_p() = p_pidfCoefs.rangeP.high-p_pidfCoefs.rangeP.low
    fun scal_i() = p_pidfCoefs.rangeI.high-p_pidfCoefs.rangeI.low
    fun scal_d() = p_pidfCoefs.rangeD.high-p_pidfCoefs.rangeD.low
    fun scal_f() = p_pidfCoefs.rangeF.high-p_pidfCoefs.rangeF.low

}