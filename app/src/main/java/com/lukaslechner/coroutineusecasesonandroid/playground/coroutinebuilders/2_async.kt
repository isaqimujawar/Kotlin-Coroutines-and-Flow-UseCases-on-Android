package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *      "Deferred = Job with Result"
 *  Deferred type is a subtype of Job type.
 *  So you can do all the things with the Deferred type that you can do with the Job Type
 *      deferred1.start()
 *      deferred1.cancel()
 *      deferred1.join()
 *      deferred1.await()
 *
 *      The call to await() will suspend the coroutine that was started with runBlocking
 *      until the first coroutine that was started with async completes
 *      and produces a value.
 */

fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis()

    val deferred1 = async(start = CoroutineStart.LAZY) {
        val result1 = networkCall(1)
        println("result1 received: $result1 after ${elapsedMillis(startTime)}")
        result1
    }
    val deferred2 = async {
        val result2 = networkCall(2)
        println("result2 received: $result2 after ${elapsedMillis(startTime)}")
        result2
    }
    val resultList: List<String> = listOf(
        deferred1.await(),
        deferred2.await()
    )

    println("Result list: $resultList after ${elapsedMillis(startTime)}")
}

suspend fun networkCall(number: Int): String {
    delay(500)
    return "Result $number"
}

fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime