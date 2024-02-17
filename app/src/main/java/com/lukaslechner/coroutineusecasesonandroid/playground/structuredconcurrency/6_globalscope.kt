package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job

fun main() {

    println("Job of GlobalScope: ${GlobalScope.coroutineContext[Job]}")

    println("Job of CoroutineScope: ${CoroutineScope(Job()).coroutineContext[Job]}")
}