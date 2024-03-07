package com.lukaslechner.coroutineusecasesonandroid.playground.flow.stateflow

import kotlinx.coroutines.flow.MutableStateFlow

fun main() {

    val counter = MutableStateFlow(0)

    println(counter.value)

    counter.value = 3

    println(counter.value)
}