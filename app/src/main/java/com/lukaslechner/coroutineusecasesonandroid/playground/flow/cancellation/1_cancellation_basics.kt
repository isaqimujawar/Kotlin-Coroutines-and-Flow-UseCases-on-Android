package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main() {
    val scope = CoroutineScope(EmptyCoroutineContext)

    val job: Job = scope.launch {
        intFlow()
            .onStart { println("Flow started") }
            .onCompletion { cause -> if (cause is CancellationException) println("Flow got cancelled.") }
            .onCompletion { if (it == null) println("Completed successfully") }
            .onEach { println("Collected $it") }
            .collect { if (it == 2) cancel() }
    }

    job.join()
}

private fun intFlow() = flow<Int> {
    emit(1)
    emit(2)
    emit(3)
}