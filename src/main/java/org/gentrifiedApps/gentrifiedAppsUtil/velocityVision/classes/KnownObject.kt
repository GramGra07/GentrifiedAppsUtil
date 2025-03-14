package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.opencv.core.MatOfPoint3f
import org.opencv.core.Point3
import org.opencv.core.Scalar

data class KnownObject (
    val size: Size3D,
    val lowerScalar: Scalar,
    val upperScalar: Scalar,
    val position:Pose2D,
    val rotationVector: RotationVector = RotationVector(0.0, 0.0, 0.0)
){
    constructor(size: Size3D, lowerScalar: Scalar, upperScalar: Scalar, position: Pose2D) : this(size, lowerScalar, upperScalar, position,
        RotationVector(0.0, 0.0, 0.0)
    )
    fun toMat3f(): MatOfPoint3f {
        return MatOfPoint3f(
            Point3(size.width, size.height, size.length),
            Point3(size.width, size.height, 0.0),
            Point3(size.width, 0.0, 0.0),
            Point3(size.width, 0.0, size.length),
            Point3(0.0, size.height, size.length),
            Point3(0.0, size.height, 0.0),
            Point3(0.0, 0.0, 0.0),
            Point3(0.0, 0.0, size.length)
        )
    }
}
