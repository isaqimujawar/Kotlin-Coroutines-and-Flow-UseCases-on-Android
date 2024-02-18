package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency.scopingfunctions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * usecase - Task 3 should start after Task 1 & Task 2 are completed.
 */
fun main() {
    val scope = CoroutineScope(Job())

    scope.launch {

        doFewTasks()

        launch {
            println("Starting Task 3")
            delay(300)
            println("Task 3 completed")
        }
    }

    Thread.sleep(1000)
}

// A suspend function using coroutineScope will run the tasks sequentially
suspend fun doFewTasks() = coroutineScope {
    launch {
        println("Starting Task 1")
        delay(100)
        println("Task 1 completed")
    }

    launch {
        println("Starting Task 2")
        delay(200)
        println("Task 2 completed")
    }
}