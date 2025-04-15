package org.gentrifiedApps.gentrifiedAppsUtil.classes.except

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

class ExceptionThrower {
    companion object {
        @JvmStatic
        fun throwException(setSet: String, exception: Exception) {
            Scribe.instance.setSet(setSet).logError("${exception.message}")
            throw exception
        }
    }
}