package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes

interface LensIntrinsics {
    var fx: Double?
    var fy: Double?
    var cx: Double?
    var cy: Double?
}

class DefaultLensIntrinsics(
    override var fx: Double? = 0.0,
    override var fy: Double? = 0.0,
    override var cx: Double? = 0.0,
    override var cy: Double? = 0.0
) : LensIntrinsics {
    constructor() : this(0.0, 0.0, 0.0, 0.0)
}