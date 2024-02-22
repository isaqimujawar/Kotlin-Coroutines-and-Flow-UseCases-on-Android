package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *  Section 9 Structured Concurrency 3 - Parents wait for children
 */

fun main() = runBlocking {
    val scope = CoroutineScope(Dispatchers.Default)

    val parentCoroutineJob = scope.launch {
        launch {
            delay(1000)
            println("Child Coroutine 1 has completed")
        }
        launch {
            delay(1000)
            println("Child Coroutine 2 has completed")
        }
    }

    parentCoroutineJob.join()
    println("Parent Coroutine has completed (this was started with launch inside scope)")

}