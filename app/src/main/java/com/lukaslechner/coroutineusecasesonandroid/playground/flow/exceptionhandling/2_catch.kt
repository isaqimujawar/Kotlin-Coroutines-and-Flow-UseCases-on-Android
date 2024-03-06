package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

/**
 *      public suspend fun Flow<*>.collect(): Unit
 *
 *     - Terminal flow operator that collects the given flow but ignores all emitted values.
 *     - If any exception occurs during collect or in the provided flow,
 *     - this exception is rethrown from this method.
 *
 *     - It is a shorthand for collect {}.
 *     - This operator is usually used with onEach, onCompletion and catch operators
 *     - to process all emitted values and handle an exception that might occur in the upstream flow or during processing,
 *
 *     - for example:
 *
 *     flow
 *         .onEach { value -> process(value) }
 *         .catch { e -> handleException(e) }
 *         .collect() // trigger collection of the flow
 */
suspend fun main(): Unit = coroutineScope {
    launch {
        val stocksFlow = techStocksFlow()

        stocksFlow
            .onCompletion { cause ->
                if (cause == null)
                    println("Flow completed successfully")
                else
                    println("Flow completed exceptionally with $cause")
            }
            .catch { throwable ->
                println("Handled exception in catch() operator - $throwable")
            }
            .collect { println("Collected: $it") }

    }
}

private fun techStocksFlow(): Flow<String> = flow {
    emit("Apple")
    emit("Microsoft")

    throw Exception("Network Connection Failed!")
}