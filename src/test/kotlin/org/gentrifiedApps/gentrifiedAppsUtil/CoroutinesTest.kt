package org.gentrifiedApps.gentrifiedAppsUtil

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Async.Companion.runAsync
import kotlin.test.Test
import kotlin.test.assertEquals

class CoroutinesTest {

    private val repository = MyRepository()

    @Test
    fun testFetchData() = runBlocking {
        val result = repository.fetchData()
        assertEquals("Success", result)
    }

    @Test
    fun testUnBlocking() = runBlocking {
        val job = launch {
            val result = repository.fetchData()
            println("Got: $result")
            assertEquals("Success", result)
        }
        job.join() // Ensures coroutine completes before test ends
    }

    @Test
    fun testAsync() = runBlocking {
        val a = async { repository.fetchData() }
        val b = async { repository.fetchData() }

        val resultA = a.await()
        val resultB = b.await()

        println("Results: $resultA and $resultB")
    }

    @Test
    fun testAsyncLib() {
        runAsync(
            listOf(
                { println(repository.fetchData()) },
                { println(repository.fetchData()) },
                { println("hello") }
            )
        )
    }
}

class MyRepository {
    suspend fun fetchData(): String {
        delay(1000) // Simulated work
        return "Success"
    }
}
