package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

suspend fun main() {
    flowOf(1, 2, 3, 4, 5, 6)
        .onEach {
            print("$it -> ")
        }
        .map {
            it * 10
        }
        .collect { collectedValue ->
            println(collectedValue)
        }
}