package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {    // Coroutine_1
    // Job is basically a reference or representation of the code that is started with launch
    val job: Job = launch(start = CoroutineStart.LAZY) {    // Coroutine_2
        networkRequest()
        println("result received")
    }
    delay(200)
    job.start()
    println("end of runBlocking")
}

suspend fun networkRequest(): String {
    delay(500)
    return "Result"
}