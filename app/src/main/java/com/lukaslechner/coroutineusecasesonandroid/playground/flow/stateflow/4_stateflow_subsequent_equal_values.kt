package com.lukaslechner.coroutineusecasesonandroid.playground.flow.stateflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 *  distinctUntilChanged operator
 *      - Returns flow where all subsequent repetitions of the same value are filtered out,
 *
 *      Note that any instance of StateFlow already behaves as if
 *      - distinctUntilChanged operator is applied to it,
 *      - so applying distinctUntilChanged to a StateFlow has no effect.
 *
 */
suspend fun main() {

    val scope = CoroutineScope(Dispatchers.Default)

    val counter = MutableStateFlow(0)

    scope.launch {
        repeat(5) {
            println("Emitting 1")
            counter.emit(1)
            delay(100)
        }
    }

    scope.launch {
        counter.collect {
            println("Collected $it")
        }
    }

    Thread.sleep(1000)
}