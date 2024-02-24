package com.lukaslechner.coroutineusecasesonandroid.playground.flow.terminaloperators

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking

fun main() {
    val flow = flow {
        delay(100)
        println("emitting first value")
        emit(1)

        delay(100)
        println("emitting second value")
         emit(2)

        delay(100)
        println("emitting third value")
        emit(1)
    }

    runBlocking {
        val item = flow.toSet()
        println("Received: $item")
    }

    runBlocking {
        val item = flow.toList()
        println("Received: $item")
    }
}