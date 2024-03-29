package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform

suspend fun main() {
    flowOf(1, 2, 3, 4, 5, 6)
        .transform {
            emit(it)
            emit(it * 10)
            if (it > 3) emit(it * 20)
        }
        .collect { collectedValue ->
            println(collectedValue)
        }
}