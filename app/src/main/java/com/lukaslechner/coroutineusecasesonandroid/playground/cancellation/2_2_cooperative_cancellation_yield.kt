package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {
        repeat(10) { index ->
            yield()
            println("operation number $index")
            Thread.sleep(100)
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}