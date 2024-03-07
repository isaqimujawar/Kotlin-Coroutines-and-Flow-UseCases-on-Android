package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows.flows_are_cold

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow


/**
 *  Cold flow Property 1 - Become active on collection
 */
suspend fun main(): Unit = coroutineScope {
    coldFlow()
        .collect { println("Collector 1 collects $it") }
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