package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Section 9 Structured Concurrency 2
 *  - Coroutines started in the same scope form a hierarchy.
 */
fun main() {
    val scopeJob = Job()
    val scope = CoroutineScope(scopeJob)

    val passedJob = Job()
    val coroutineJob = scope.launch(passedJob) {
        println("Starting coroutine")
        delay(1000)
    }

    Thread.sleep(100)
    println("passedJob and coroutineJob are references to the same object: ${passedJob === coroutineJob}")
    println("Is coroutineJob a child of scopeJob => ${scopeJob.children.contains(coroutineJob)}")
    println("Is passedJob a child of scopeJob => ${scopeJob.children.contains(passedJob)}")
        println("Is coroutineJob a child of passedJob => ${passedJob.children.contains(coroutineJob)}")
}