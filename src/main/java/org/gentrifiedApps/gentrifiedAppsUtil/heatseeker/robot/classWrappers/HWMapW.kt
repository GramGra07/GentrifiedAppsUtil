// Kotlin
package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

class HWMapW {

    @JvmOverloads
    @Suppress("UNCHECKED_CAST")
    fun <T : FTCWrapper> get(cls: Class<T>, name: String, port: Int? = null): T {
        // Try (String, int)/(String, Integer)
        if (port != null) {
            try {
                val c = cls.getConstructor(String::class.java, Int::class.javaPrimitiveType)
                return c.newInstance(name, port)
            } catch (_: NoSuchMethodException) {
            }
            try {
                val c = cls.getConstructor(String::class.java, java.lang.Integer::class.java)
                return c.newInstance(name, port)
            } catch (_: NoSuchMethodException) {
            }
        }
        // Try (String)
        try {
            val c = cls.getConstructor(String::class.java)
            return c.newInstance(name)
        } catch (_: NoSuchMethodException) {
        }

        // Try ()
        try {
            val c = cls.getConstructor()
            return c.newInstance()
        } catch (_: NoSuchMethodException) {
        }

        throw IllegalArgumentException(
            "No suitable constructor found for ${cls.simpleName}. Expected one of: (String, Int), (String), or ()."
        )
    }

    inline fun <reified T : FTCWrapper> get(name: String, port: Int? = null): T =
        get(T::class.java, name, port)
}