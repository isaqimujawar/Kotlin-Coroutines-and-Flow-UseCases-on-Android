package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *       A coroutine is an instance of a suspendable computation.
 *
 *       - Cancelling a parent will cancel all children.
 *       - Cancelling a child won't cancel the parent or siblings.
 *
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.Default)
    scope.coroutineContext[Job]?.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) println("Parent Job was cancelled")

    }

    // Coroutine 1
    val childJob1 = scope.launch {
        delay(1000)
        println("Coroutine 1 has completed")
    }

    childJob1.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) println("Coroutine 1 was cancelled")
    }

    // Coroutine 2
    scope.launch {
        delay(1000)
        println("Coroutine 2 has completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) println("Coroutine 2 was cancelled")
    }

    delay(200)
    childJob1.cancelAndJoin()

    // scope.coroutineContext[Job]?.cancelAndJoin()
}