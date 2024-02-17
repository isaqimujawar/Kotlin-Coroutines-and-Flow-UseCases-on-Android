package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Section 9 Structured Concurrency 1 - Coroutines need to be started in a scope
 */

val scope = CoroutineScope(Dispatchers.Default)

fun main() = runBlocking {
    val job = scope.launch {
        delay(100)
        println("Coroutine Completed!")
    }

    job.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine was cancelled!")
        } else {
            println("Coroutine was completed!")
        }
    }

    delay(50)
    onDestroy()
}

fun onDestroy() {
    println("Life-time of coroutine ends!")
    scope.cancel()
}