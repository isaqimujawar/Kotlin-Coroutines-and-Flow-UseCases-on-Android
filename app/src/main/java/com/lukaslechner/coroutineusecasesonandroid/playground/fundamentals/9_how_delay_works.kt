package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() {
    println("main starts")

    delayDemonstration(1, 500)
    delayDemonstration(2, 300)

    println("main ends")
}

fun delayDemonstration(number: Int, delay: Long) {
    println("Coroutine $number starts work")

    // delay(delay)
    Handler(Looper.getMainLooper())
        .postDelayed(
            /* runnable = */ { println("Coroutine $number has finished") },
            /* delayMillis = */ delay
        )
    println("Coroutine $number has finished")
}