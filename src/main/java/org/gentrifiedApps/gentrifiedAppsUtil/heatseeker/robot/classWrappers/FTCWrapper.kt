package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

internal abstract class FTCWrapper {
    constructor(
        name: String, port: Int
    ) {
        this.name = name
        this.port = port
    }

    var name: String
    var port: Int = -1

}