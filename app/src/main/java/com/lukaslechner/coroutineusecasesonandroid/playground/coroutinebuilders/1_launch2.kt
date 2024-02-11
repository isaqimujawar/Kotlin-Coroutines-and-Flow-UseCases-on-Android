package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis()

    val resultList = mutableListOf<String>()

    // call to networkCall() in launch is non-blocking and will run in parallel
    val job1 = launch {
        val result1 = networkCall(1)
        resultList.add(result1)
        println("result1 received: $result1 after ${elapsedMillis(startTime)}")
    }
    val job2 = launch {
        val result2 = networkCall(2)
        resultList.add(result2)
        println("result2 received: $result2 after ${elapsedMillis(startTime)}")
    }

    job1.join()         // runBlocking will wait for coroutine to complete before moving on
    job2.join()         // runBlocking will wait for coroutine to complete before moving on

    println("Result list: $resultList after ${elapsedMillis(startTime)}")
}