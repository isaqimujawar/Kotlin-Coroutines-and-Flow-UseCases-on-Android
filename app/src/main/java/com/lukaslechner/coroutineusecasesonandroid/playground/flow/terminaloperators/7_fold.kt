package com.lukaslechner.coroutineusecasesonandroid.playground.flow.terminaloperators

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
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
        val item = flow.fold(5) { accumulator, emittedValue ->
            accumulator + emittedValue
        }
        println("Received: $item")
    }
}