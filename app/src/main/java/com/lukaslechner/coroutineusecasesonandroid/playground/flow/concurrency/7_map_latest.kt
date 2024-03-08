package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

/**
 *  Flow.mapLatest{}
 *      - mapLatest{} is useful in situations where we have a slow operation in its block and we again don't care about outdated emissions.
 *
 *
 *      Only Pancake 5 reaches the Collector.
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
    }.mapLatest {
        println("Add topping onto the Pancake $it")
        delay(200)      // mimicking a computation
        it
    }

    flow.collect {
        println("Collector:     Start eating pancake $it")
        delay(300)
        println("Collector:     Finished eating pancake $it")
    }
}