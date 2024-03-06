package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

suspend fun main(): Unit = coroutineScope {
    val stocksFlow = techStocksFlow()

    stocksFlow
        .onCompletion { cause ->
            if (cause == null)
                println("Flow completed successfully")
            else
                println("Flow completed exceptionally with $cause")
        }
        .onEach {
            throw Exception("Exception in collect{}")
            println("Collected: $it")
        }
        .catch { throwable ->
            println("Handled exception in catch() operator - $throwable")
        }
        .launchIn(this)
}

private fun techStocksFlow(): Flow<String> = flow {
    emit("Apple")
    emit("Microsoft")

    throw Exception("Network Connection Failed!")
}

