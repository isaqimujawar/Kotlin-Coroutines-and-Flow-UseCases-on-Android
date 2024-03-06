package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.math.BigInteger
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main() {
    val scope = CoroutineScope(EmptyCoroutineContext)

    val job: Job = scope.launch {
        intFlow()
            .onStart { println("Flow started") }
            .onCompletion { cause -> if (cause is CancellationException) println("Flow got cancelled.") }
            .onCompletion { if (it == null) println("Completed successfully") }
            .onEach { println("Collected $it") }
            .collect { if (it == 2) cancel() }
    }

    job.join()
}

private fun intFlow() = flow<Int> {
    emit(1)
    emit(2)

    println("Calculation started")
    calculateFactorial(1_000)
    println("Calculation finished")

    emit(3)
}


/**
 *  Cooperative Cancellation -
 *  Making this function Cooperative Regarding Cancellation using ensureActive()
 *  - because user-defined suspend function are not by default checking if the Coroutine is Active or not.
 *  - The suspend function that are part of the Kotlin Coroutine Standard Library are by default Cooperative
 *  and are internally checking whether the Coroutine is still Active or not.
 */
private suspend fun calculateFactorial(number: Int) = coroutineScope {
    var factorial = BigInteger.ONE
    for (i in 1..number) {
        ensureActive()
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
    }
    factorial
}