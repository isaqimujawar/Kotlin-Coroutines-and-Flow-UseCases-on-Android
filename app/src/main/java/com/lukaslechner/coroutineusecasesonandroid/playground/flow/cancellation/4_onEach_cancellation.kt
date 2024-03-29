package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
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
            .onEach {
                println("Received $it in onEach")

                // check if the Coroutine is still Active
                // if (!currentCoroutineContext().job.isActive) throw CancellationException()
                ensureActive()
            }
            .collect {
                println("Collected $it")
                if (it == 2) cancel()
            }
    }

    job.join()
}