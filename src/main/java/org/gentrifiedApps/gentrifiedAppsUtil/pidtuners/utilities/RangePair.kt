package com.dacodingbeast.pidtuners.utilities

data class RangePair<A>(val low:A,val high:A){
    fun output():String{
        return "RangePair(low=$low, high=$high)"
    }
}