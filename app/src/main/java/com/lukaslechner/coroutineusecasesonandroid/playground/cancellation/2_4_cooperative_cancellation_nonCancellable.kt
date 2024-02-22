package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {
        repeat(10) { index ->
            if (isActive) {
                println("operation number $index")
                Thread.sleep(100)
            } else {
                // return@launch

                // if isActive == false, Cleaning operations can be performed here
                withContext(NonCancellable) {
                    delay(100)
                    println("Cleaning up...")
                    throw CancellationException()
                }
            }
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}