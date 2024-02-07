package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() = runBlocking {
    println("main starts")
    joinAll(
        async { threadSwitchingCoroutine(1, 500) },
        async { threadSwitchingCoroutine(2, 300) }
    )
    println("main ends")
}

 suspend fun threadSwitchingCoroutine(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)        // Suspension point: bcoz, delay() is a suspend function.
     withContext(Dispatchers.Default) {
         println("Coroutine $number has finished work on ${Thread.currentThread().name}")
     }
}