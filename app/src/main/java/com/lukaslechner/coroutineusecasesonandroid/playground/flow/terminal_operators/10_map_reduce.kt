package com.lukaslechner.coroutineusecasesonandroid.playground.flow.terminal_operators

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.runBlocking

fun main() {
    val flow = flow {
        delay(100)
        println("emitting first value")
        emit(1)

        delay(100)
        println("emitting second value")
        emit(2)
    }

    runBlocking {
        val item = flow
            .map { emittedValue -> emittedValue * 10 }
            .reduce { accumulator, mapValue -> accumulator + mapValue }
        println("Received: $item")
    }
}