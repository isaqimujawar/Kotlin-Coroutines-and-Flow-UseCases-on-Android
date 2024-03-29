package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    launch {
        val stocksFlow = techStocksFlow()
            .map {
                throw Exception("Exception in Map")
            }

        try {
            stocksFlow
                .onCompletion { cause ->
                    if(cause == null)
                        println("Flow completed successfully")
                    else
                        println("Flow completed exceptionally with $cause")
                }
                .collect { println("Collected: $it") }
        } catch (e: Exception) {
            println("Handled Exception in catch block - $e")
        }
    }
}

private fun techStocksFlow(): Flow<String> = flow {
    emit("Apple")
    emit("Microsoft")
    emit("Alphabet")

    throw Exception("Network Connection Failed!")
}