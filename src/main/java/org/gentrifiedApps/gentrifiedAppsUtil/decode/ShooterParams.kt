package org.gentrifiedApps.gentrifiedAppsUtil.decode

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.DirectionalVector

data class ShooterParams(
    var shooterVector: DirectionalVector,
    var shooterRotationVector: EasyRotationVector
)