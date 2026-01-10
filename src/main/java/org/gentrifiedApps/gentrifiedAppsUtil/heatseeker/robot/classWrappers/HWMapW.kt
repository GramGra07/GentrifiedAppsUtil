package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

class HWMapW {

    @JvmOverloads
    @Suppress("UNCHECKED_CAST")
    fun <T : FTCWrapper> get(cls: Class<T>, name: String): T {
        // Try (String, int)/(String, Integer)
        try {
            val c = cls.getConstructor(String::class.java)
            return c.newInstance(name)
        } catch (_: NoSuchMethodException) {
        }


        throw IllegalArgumentException(
            "No suitable constructor found for ${cls.simpleName}. Expected one of: (String, Int), (String), or ()"
        )
    }
}