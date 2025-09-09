package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.caching.CacheManager
import org.gentrifiedApps.gentrifiedAppsUtil.classes.caching.CacheState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CacheManagerTest {

    @Test
    fun initialStateIsNotCachedAndCacheIsNull() { // Changed
        var functionCallCount = 0
        val cacheManager = CacheManager {
            functionCallCount++
            "initialValue"
        }
        assertEquals(CacheState.NOT_CACHED, cacheManager.state)
        assertNotNull(cacheManager.loadCache())
        assertEquals(1, functionCallCount)
    }

    @Test
    fun getCacheBuildsCacheIfNotCachedAndReturnsCorrectValue() { // Changed
        var functionCallCount = 0
        val expectedValue = "testData"
        val cacheManager = CacheManager {
            functionCallCount++
            expectedValue
        }

        val result = cacheManager.loadCache()

        assertEquals(expectedValue, result)
        assertEquals(CacheState.CACHED, cacheManager.state)
        assertEquals(expectedValue, cacheManager.loadCache())
        assertEquals(1, functionCallCount)
    }

    @Test
    fun getCacheReturnsCachedValueWithoutRebuilding() { // Changed
        var functionCallCount = 0
        val initialValue = "cachedValue"
        val cacheManager = CacheManager {
            functionCallCount++
            initialValue
        }

        // First call to build the cache
        cacheManager.loadCache()
        assertEquals(1, functionCallCount)

        // Second call
        val result = cacheManager.loadCache()

        assertEquals(initialValue, result)
        assertEquals(CacheState.CACHED, cacheManager.state)
        assertEquals(initialValue, cacheManager.loadCache())
        assertEquals(1, functionCallCount)
    }

    @Test
    fun refreshCacheRebuildsCache() { // Changed
        var functionCallCount = 0
        var currentValue = "firstValue"
        val cacheManager = CacheManager {
            functionCallCount++
            currentValue
        }

        // Initial cache build
        cacheManager.loadCache()
        assertEquals("firstValue", cacheManager.loadCache())
        assertEquals(1, functionCallCount)

        // Change the value that the function will produce
        currentValue = "secondValue"
        cacheManager.refreshCache()

        assertEquals("secondValue", cacheManager.loadCache())
        assertEquals(CacheState.CACHED, cacheManager.state)
        assertEquals(2, functionCallCount)

        // Verify getCache now returns the new refreshed value
        assertEquals("secondValue", cacheManager.loadCache())
        assertEquals(2, functionCallCount)
    }

    @Test
    fun dumpCacheClearsCacheStateAndValue() { // Changed
        var functionCallCount = 0
        val cacheManager = CacheManager {
            functionCallCount++
            "someData"
        }

        // Build the cache
        cacheManager.loadCache()
        assertNotNull(cacheManager.loadCache())
        assertEquals(CacheState.CACHED, cacheManager.state)
        assertEquals(1, functionCallCount)

        cacheManager.dumpCache()

        assertNotNull(cacheManager.loadCache())
        assertEquals(CacheState.CACHED, cacheManager.state)
        assertEquals(2, functionCallCount)

        // Verify getCache will rebuild after dump
        val newValue = cacheManager.loadCache()
        assertEquals("someData", newValue)
        assertEquals(2, functionCallCount)
        assertEquals(CacheState.CACHED, cacheManager.state)
    }

    @Test
    fun refreshCacheWorksCorrectlyWhenCacheIsInitiallyEmpty() { // Changed
        var functionCallCount = 0
        val expectedValue = "refreshedValue"
        val cacheManager = CacheManager {
            functionCallCount++
            expectedValue
        }

        assertEquals(CacheState.NOT_CACHED, cacheManager.state)

        cacheManager.refreshCache() // Should build the cache

        assertEquals(expectedValue, cacheManager.loadCache())
        assertEquals(CacheState.CACHED, cacheManager.state)
        assertEquals(1, functionCallCount)
    }
}