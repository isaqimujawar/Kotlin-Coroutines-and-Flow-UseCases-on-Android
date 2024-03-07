package com.lukaslechner.coroutineusecasesonandroid.playground.flow.stateflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun main() {
    val scope = CoroutineScope(Dispatchers.Default)

    val counter = MutableStateFlow(99)

    scope.launch {
        repeat(5) {
            counter.emit(it)
            delay(200)
        }
    }

    scope.launch {
        counter.collect {
            println("Collected $it")
        }
    }

    Thread.sleep(1000)
}