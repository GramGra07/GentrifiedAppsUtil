package org.gentrifiedApps.gentrifiedAppsUtil.classes.caching

enum class CacheState {
    CACHED,
    NOT_CACHED
}

class CacheManager<T>(val function: () -> T) {
    init {
        require(function != null) { "Function cannot be null" }
    }

    internal var state = CacheState.NOT_CACHED
    private var cache: T? = null
    fun loadCache(): T {
        if (state == CacheState.NOT_CACHED) {
            buildCache()
        }
        return cache!!
    }

    fun dumpCache() {
        cache = null;
        state = CacheState.NOT_CACHED;
    }

    fun refreshCache() {
        if (cache != null) dumpCache()
        buildCache()
    }

    fun buildCache() {
        cache = function()
        state = CacheState.CACHED
    }
}