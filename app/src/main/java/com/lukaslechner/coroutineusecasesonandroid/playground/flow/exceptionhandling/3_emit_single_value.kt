package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

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
                emit("Default Stock")
            }
            .collect { println("Collected: $it") }

    }
}

private fun techStocksFlow(): Flow<String> = flow {
    emit("Apple")
    emit("Microsoft")

    throw Exception("Network Connection Failed!")
    emit("Alphabet (Google)")
}