package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows.flows_are_cold

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

/**
 *  Cold flow Property 2 - Become inactive on cancellation of the collecting Coroutine.
 */

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        coldFlow()
            .onCompletion { println("Flow of Collector 1 completed") }
            .collect { println("Collector 1 collects $it") }
    }

    delay(1500)
    job.cancelAndJoin()     // Cancels the job and suspends the invoking coroutine until the cancelled job is complete.
}

private fun coldFlow() = flow {
    println("Emitting 1")
    emit(1)

    delay(1000)
    println("Emitting 2")
    emit(2)

    delay(1000)
    println("Emitting 3")
    emit(3)
}
