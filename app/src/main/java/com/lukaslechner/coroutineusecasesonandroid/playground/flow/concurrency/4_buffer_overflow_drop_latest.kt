package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow

/**
 *  Buffers Overflow Strategy - DROP_LATEST
 *      - Pancakes 3, 5 are dropped
 *      - Pancakes 1, 2, 4 are Eaten
 */
suspend fun main() = coroutineScope {

    val flow = flow {
        repeat(5) {
            val pancakeIndex = it + 1
            println("Emitter:       Start Cooking Pancake $pancakeIndex")
            delay(100)
            println("Emitter:       Pancake $pancakeIndex ready")
            emit(pancakeIndex)
        }
    }.buffer(capacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    flow.collect {
        println("Collector:     Start eating pancake $it")
        delay(300)
        println("Collector:     Finished eating pancake $it")
    }
}