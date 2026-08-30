package com.dacodingbeast.pidtuners.utilities.MathFunctions

class PhysicsMath {
    companion object{

        final val G = 9.81
        fun I(armMass: Double, length: Double, manipMass: Double):Double = ((0.33)*armMass*length*length+(length*length * manipMass))
        fun kG(armMass: Double, length: Double, manipMass: Double):Double = (G*(armMass*length/2+manipMass*length))
    }
}