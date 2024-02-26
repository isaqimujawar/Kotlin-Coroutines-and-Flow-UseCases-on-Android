package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile

suspend fun main() {
    flowOf(1, 2, 3, 4, 5, 6)
        .take(3)
        .collect { collectedValue ->
            println(collectedValue)
        }

    flowOf(1, 2, 3, 4, 5, 6)
        .takeWhile { it < 3 }
        .collect { collectedValue ->
            println(collectedValue)
        }
}