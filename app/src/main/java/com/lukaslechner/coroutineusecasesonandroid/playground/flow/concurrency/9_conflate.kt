package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow

/**
 *  The conflate() operator
 *      - collector always gets the most recent value emitted.
 *
 *      - Pancakes 1, 3, 5 are eaten
 *      - Pancakes 2, 4 are dropped
 *
 *      - Note that conflate() operator is a shortcut for -
 *      - buffer(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
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
    }.conflate()

    flow.collect {
        println("Collector:     Start eating pancake $it")
        delay(300)
        println("Collector:     Finished eating pancake $it")
    }
}