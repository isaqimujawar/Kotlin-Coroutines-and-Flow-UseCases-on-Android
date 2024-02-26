package com.lukaslechner.coroutineusecasesonandroid.playground.flow.terminal_operators

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext


/**
 *
 *  flow.launchIn(scope) is a concise way of writing scope.launch { flow.collect() }
 */
suspend fun main() {
    val flow = flow {
        delay(100)

        println("emitting first value")
        emit(1)

        delay(100)

        println("emitting second value")
        emit(2)
    }

    val scope = CoroutineScope(EmptyCoroutineContext)

    flow
        .onEach { println("Received $it with launchIn - 1") }
        .launchIn(scope)

    flow
        .onEach { println("Received $it with launchIn - 2") }
        .launchIn(scope)


    /*scope.launch {
        flow.collect {
            println("Received $it in collect - 1")
        }

        // the coroutine started with launch coroutine is suspended/paused
        // until the first flow is collected/completed.

        flow.collect {
            println("Received $it in collect - 2")
        }
    }*/

    Thread.sleep(1000)
}