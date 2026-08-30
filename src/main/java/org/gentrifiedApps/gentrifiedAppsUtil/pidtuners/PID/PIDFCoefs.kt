package com.dacodingbeast.pidtuners.PID

import com.dacodingbeast.pidtuners.utilities.RangePair

data class PivotPIDFCoefs(var p:Double, var rangeP: RangePair<Double>, var i:Double, var rangeI: RangePair<Double>, var d:Double, var rangeD: RangePair<Double>, var f:Double, var rangeF: RangePair<Double>){
    fun output():String{
        return "p: $p, rangeP: ${rangeP.output()}, i: $i, rangeI: ${rangeI.output()}, d: $d, rangeD: ${rangeD.output()}, f: $f, rangeF: ${rangeF.output()}"
    }
    companion object{
        fun zeros(): PivotPIDFCoefs{
            return PivotPIDFCoefs(0.0, RangePair(0.0,0.0), 0.0, RangePair(0.0,0.0), 0.0, RangePair(0.0,0.0), 0.0, RangePair(0.0,0.0))
        }
    }
}

data class PIDFCoefs(var p:Double, var i: Double,var d: Double, var f: Double){
    fun output(): String{
        return "p: $p, i: $i, d: $d, f: $f"
    }
    companion object{
        fun zeros(): PIDFCoefs{
            return PIDFCoefs(0.0,0.0,0.0,0.0)
        }
    }
}