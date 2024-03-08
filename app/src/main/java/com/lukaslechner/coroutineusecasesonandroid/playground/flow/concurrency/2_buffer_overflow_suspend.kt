package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow

/**
 *  Buffers Overflow Strategy - SUSPEND
 *      - No Pancakes are dropped
 *      - But the Emitter is suspended when the buffer is full.
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
    }.buffer(capacity = 1, onBufferOverflow = BufferOverflow.SUSPEND)

    flow.collect {
        println("Collector:     Start eating pancake $it")
        delay(300)
        println("Collector:     Finished eating pancake $it")
    }
}