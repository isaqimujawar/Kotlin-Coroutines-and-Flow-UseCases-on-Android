package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

suspend fun main() = coroutineScope {

    val flow = flow {

        println("emitting 1")
        emit(1)

        delay(100)
        println("emitting 2")
        emit(2)

        delay(100)
        println("emitting 3")
        emit(3)
    }

    flow.collect {
        println("Started collecting $it")
        delay(300)
        println("Finished finished collecting $it")
    }
}