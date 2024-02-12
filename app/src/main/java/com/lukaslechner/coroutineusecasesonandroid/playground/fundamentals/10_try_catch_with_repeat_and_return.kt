package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    tryCatchWithRepeatAndReturn(2) {
        blockOfCode(it)
    }
}

private suspend fun tryCatchWithRepeatAndReturn(times: Int, block: suspend (Int) -> Unit) {
    repeat(times) {
        try {
            return block(it)
        } catch (exception: Exception) {
            println(exception.localizedMessage)
        }
    }
    return block(45)        // this line of code will only be reached/executed, if the return statement inside the try block is not executed.
}

@Throws(Exception::class)
private fun blockOfCode(input: Int) = when(input) {
    0 -> throw Exception("Hi! This is an exception! repeat = $input")
    1 -> throw Exception("Hi! This is an exception! repeat = $input")
    2 -> println("inside try block of repeat() = $input")
    else -> println("outside try block of repeat() = $input")
}