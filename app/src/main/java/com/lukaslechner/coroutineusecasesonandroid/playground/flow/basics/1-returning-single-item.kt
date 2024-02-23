package com.lukaslechner.coroutineusecasesonandroid.playground.flow.basics

import java.math.BigInteger

fun main() {
    val result = calculateFactorial(5)
    println("Result: $result")
}

private fun calculateFactorial(number: Int): BigInteger {
    var factorial = BigInteger.ONE
    for (i in 1..number){
        Thread.sleep(10)
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
    }
    return factorial
}
