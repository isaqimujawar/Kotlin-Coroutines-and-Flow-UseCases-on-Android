package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.math.BigInteger
import kotlin.coroutines.EmptyCoroutineContext

/**
 *      cancel()
 *      - Cancels this scope, including its job and all its children.
 */
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

    currentCoroutineContext().ensureActive()        // throws CancellationException if not Active

    println("Calculation started")
    calculateFactorial(1_000)
    println("Calculation finished")

    emit(3)
}

private fun calculateFactorial(number: Int): BigInteger {
    var factorial = BigInteger.ONE
    for (i in 1..number) {
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
    }
    return factorial
}