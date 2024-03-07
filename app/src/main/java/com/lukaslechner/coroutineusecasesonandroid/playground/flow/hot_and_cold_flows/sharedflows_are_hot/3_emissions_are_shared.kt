package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows.sharedflows_are_hot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Hot Flow Property 3 - Emissions are shared between all collectors.
 *  - That's the reason why they are called SharedFlows.
 */
fun main() {
    val sharedFlow = MutableSharedFlow<Int>()

    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {
        repeat(5) {
            println("SharedFlow emits $it")
            sharedFlow.emit(it)
            delay(200)
        }
    }

    scope.launch {
        sharedFlow.collect {
            println("Collected $it in Collector 1")
        }
    }

    scope.launch {
        sharedFlow.collect {
            println("Collected $it in Collector 2")
        }
    }

    Thread.sleep(1500)
}