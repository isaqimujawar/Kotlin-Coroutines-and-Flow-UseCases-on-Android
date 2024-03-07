package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows.sharedflows_are_hot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Hot Flow Property 1 - Are active regardless of whether there are collectors.
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

    Thread.sleep(1500)
}