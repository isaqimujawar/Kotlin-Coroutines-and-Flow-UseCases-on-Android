package com.lukaslechner.coroutineusecasesonandroid.playground.flow.builders

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 *      Flow Builders with examples
 *      1. flowOf() - Creates a flow that produces the given value.
 *      2. .asFlow() - Creates a cold flow that produces values from the given iterable.
 *      3. flow{} - Creates a cold flow from the given suspendable block.
 */
suspend fun main() {
    // 1. Flow Builder - flowOf()
    val firstFlow = flowOf<Int>(1).collect { emittedValue ->
        println("firstFlow: $emittedValue")
    }

    val secondFlow: Flow<Int> = flowOf<Int>(1, 2, 3)

    secondFlow.collect { emittedValue ->
        println("secondFlow: $emittedValue")
    }

    // 2. Flow Builder - .asFlow()
    listOf("Apple", "Ball", "Cat").asFlow().collect { emittedValue -> println("asFlow: $emittedValue") }

    // 3. Flow Builder - flow{}
    flow {
        emit("one")
        delay(100)
        emit("two")
    }.collect { emittedValue ->
        println("flow{}: $emittedValue")
    }

    flow {
        delay(2000)
        emit("item emitted after 2000ms")

        emit("collect and emit other flows")
        secondFlow.collect { emittedValue -> emit(emittedValue) }

        emit("emitAll() concise way to emit other flows")
        emitAll(secondFlow)
    }.collect { emittedValue ->
        println("flow{}: $emittedValue")
    }
}