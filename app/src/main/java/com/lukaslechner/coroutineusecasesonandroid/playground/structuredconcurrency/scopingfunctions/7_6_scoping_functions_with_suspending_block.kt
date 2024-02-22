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

        coroutineScope {

            doTasks {
                launch {
                    println("Starting Task 1")
                    delay(100)
                    println("Task 1 completed")
                }
            }

            doTasks {
                launch {
                    println("Starting Task 2")
                    delay(200)
                    println("Task 2 completed")
                }
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

// A suspend function that takes a suspending block as its input parameter
suspend fun doTasks(block: suspend () -> Unit) {
    block()
}