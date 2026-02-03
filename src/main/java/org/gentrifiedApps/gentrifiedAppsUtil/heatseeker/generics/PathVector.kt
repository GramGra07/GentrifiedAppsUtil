package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle

class PathVector @JvmOverloads constructor(
    val translationalVector: TranslationalVector = TranslationalVector(
        0.0,
        0.0
    ), val rotationalVector: Angle = Angle.blank()
)