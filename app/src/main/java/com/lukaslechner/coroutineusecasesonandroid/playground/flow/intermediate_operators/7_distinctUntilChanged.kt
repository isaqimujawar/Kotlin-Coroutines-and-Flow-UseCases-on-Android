package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf

suspend fun main() {
    flowOf(1, 1, 2, 2, 3, 4, 3)
        .distinctUntilChanged()
        .collect { collectedValue ->
            println(collectedValue)
        }
}