package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    launch {
        stocksFlow()
            .onEach { stockData ->
                println("Collected $stockData")
            }
            .catch { cause ->
                println("Handle Exception in catch() operator: ${cause.message}")
            }
            .collect {}
    }
}

private fun stocksFlow(): Flow<String> = flow {
    repeat(5) { index ->

        delay(1000)     // Network call

        if (index < 4)
            emit("New Stock data")
        else
            throw NetworkException("Network Request Failed!")
    }
}.retryWhen { cause, attempt ->
    println("Enter retry{} with ${cause.message} and attempt $attempt")
    delay(1000 * (attempt + 1))
    cause is NetworkException
}