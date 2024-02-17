package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    val scopeJob = Job()
    val scope = CoroutineScope(scopeJob)

    var childCoroutineJob: Job? = null
    val coroutineJob = scope.launch {
        childCoroutineJob = launch {
            println("Starting child coroutine")
            delay(1000)
        }

        println("Starting coroutine")
        delay(1000)
    }

    Thread.sleep(500)
    println("Is coroutineJob a child of scopeJob => ${scopeJob.children.contains(coroutineJob)}")
    println("Is childCoroutineJob a child of coroutineJob => ${coroutineJob.children.contains(childCoroutineJob)}")
}