package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency.scopingfunctions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * use coroutineScope{} - if on failure you want cancel siblings and the scopesc
 * use supervisorScope{} - if on failure you don't want to cancel the siblings and the scope
 *
 *  usecase - Task 3 should start after Task 1 & Task 2 are completed.
 *
 */
fun main() {
    val scope = CoroutineScope(Job())

    scope.launch {

        coroutineScope {
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

        launch {
            println("Starting Task 3")
            delay(300)
            println("Task 3 completed")
        }
    }

    Thread.sleep(1000)
}