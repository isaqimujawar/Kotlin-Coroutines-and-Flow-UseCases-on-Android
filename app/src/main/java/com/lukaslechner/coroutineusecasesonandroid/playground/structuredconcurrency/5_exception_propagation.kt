package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.RuntimeException

 val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
     println("Caught exception $throwable")
 }

fun main() {
    val scope =
        CoroutineScope(context = SupervisorJob() + exceptionHandler)

    scope.launch {
        println("Coroutine 1 starts")
        delay(50)
        println("Coroutine 1 fails")
        throw RuntimeException("Coroutine 1 has a runtime exception")
    }

    scope.launch {
        println("Coroutine 2 starts")
        delay(500)
        println("Coroutine 2 completed")
    }.invokeOnCompletion {
        if (it is CancellationException)
            println("Coroutine 2 was cancelled")
    }

    Thread.sleep(1000)

    println("Scope is active: ${scope.isActive}")
}