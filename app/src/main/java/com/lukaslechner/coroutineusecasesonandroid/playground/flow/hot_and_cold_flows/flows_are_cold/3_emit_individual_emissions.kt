package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows.flows_are_cold

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

/**
 *  Cold flow Property 3 - Emit individual emissions to every collector.
 *    - In other words, every Collector gets its own stream of data.
 *    - In other words, the code in the flow{} builder is executed for every collector.
 */

suspend fun main(): Unit = coroutineScope {
    launch {
        coldFlow()
            .onCompletion { println("Flow of Collector 1 completed") }
            .collect { println("Collector 1 collects $it") }
    }

    launch {
        coldFlow()
            .onCompletion { println("Flow of Collector 2 completed") }
            .collect { println("Collector 2 collects $it")}
    }

    joinAll()
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
