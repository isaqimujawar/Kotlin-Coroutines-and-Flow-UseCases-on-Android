package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOf

suspend fun main() {
    flowOf(1, 2, 3, 4, 5, 6)
        .filter { it > 3 }
        .collect { collectedValue ->
            println(collectedValue)
        }


    // other filters that we can apply on a Flow
    flowOf(1, 2, 3, 4, 5, 6)
        .filterNot { it > 3 }
//        .filterNotNull()
//        .filterIsInstance<Int>()
        .collect { collectedValue ->
            println(collectedValue)
        }
}