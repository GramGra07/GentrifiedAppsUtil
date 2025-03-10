package test.kotlin.classes

//class IdlerTest {
//
//    @Test
//    fun testSafeIdle() {
//        var counter = 0
//        val updateWhileIdling = Runnable { counter++ }
//        val idler = Idler()
//
//        val idleTime = 1.0 // 1 second
//        idler.safeIdle(idleTime,updateWhileIdling)
//
//        // Check if the counter has been incremented
//        assertTrue("Counter should be greater than 0", counter > 0)
//    }
//
//    @Test
//    fun testSafeIdleMultiple() {
//        var counter = 0
//        val updateWhileIdling = Runnable { counter++ }
//        val idler = Idler()
//
//        val idleTime = 1.0 // 1 second
//        val iterations = 5
//        for (i in 1..iterations) {
//            idler.safeIdle(idleTime,updateWhileIdling)
//        }
//
//        // Check if the counter has been incremented
//        assertTrue("Counter should be greater than 0", counter > 0)
//        println(counter)
//    }
//}