package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes

data class Size3D (
    val width:Double = 0.0,
    val length:Double = 0.0,
    val height:Double = 0.0
){
    constructor() : this(0.0, 0.0, 0.0)
}