package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main() {
    val scope = CoroutineScope(EmptyCoroutineContext)

    val job: Job = scope.launch {
        flowOf(1, 2, 3)
            .onStart { println("Flow started") }
            .onCompletion { cause -> if (cause is CancellationException) println("Flow got cancelled.") }
            .onCompletion { if (it == null) println("Completed successfully") }
            .cancellable()
            .collect {
                println("Collected $it")
                if (it == 2) cancel()
            }
    }

    job.join()
}