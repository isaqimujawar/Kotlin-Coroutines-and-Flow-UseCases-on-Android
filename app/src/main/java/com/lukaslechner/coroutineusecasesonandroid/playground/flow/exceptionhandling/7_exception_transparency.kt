package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


/**
 * Exception in thread "main" java.lang.IllegalStateException: Flow exception transparency is violated:
 *     - Previous 'emit' call has thrown exception java.lang.Exception: Exception in collect{},
 *     - but then emission attempt of value '2' has been detected.
 *     - Emissions from 'catch' blocks are prohibited in order to avoid unspecified behaviour,
 *     - 'Flow.catch' operator can be used instead.
 *
 *
 *  Use the catch{} operator. Because -
 *  - The catch operator only catches upstream exceptions, but passes all downstream exceptions.
 *  - if there is an Exception in the Downstream then Flow will stop emitting new values.
 *  - To catch the exception in the Downstream -
 *  - We can use another catch{} operator after the onEach() operator and
 *  - Extract all the code of the collect{} block into an onEach{} block.
 */
suspend fun main() = coroutineScope {

    // example illustrating emit() simply is regular function call to collect{}
    // also how we can violate Exception Transparency.
    /*flow {
        try {
            emit(1)
        } catch (e: Exception) {
            println("Catch block: $e")
            emit(2)     // Violates Exception Transparency
        }
    }.collect { collectedValue ->
        throw Exception("Exception in collect{}")
    }*/

    // example shows the use of catch{} operator to catch upstream exceptions and not violate Exception Transparency.
    flow {
        emit(1)
    }.catch {
        println("Handled Upstream Exception in catch operator - $it")
        // if there is an Exception in the Downstream then Flow will stop emitting new values.
        // emit(2)
    }.collect {
        throw Exception("Exception in collect{} - collectedValue is $it")
    }

    // example shows to combination of onEach{}, collect{} and catch{} to handle exceptions
    /*
    flow {
        throw Exception("Exception in Flow Builder")
        emit(1)
    }.catch {
        println("Handled Upstream Exception in catch operator - $it")
        emit(2)
    }.onEach {
        // Extract all the code of the collect{} block into an onEach{} block.
        throw Exception("Exception in collect{} - collectedValue is $it")
    }.catch {
        // And Use another catch{} operator to handle exceptions in the onEach{}.
        println("Handled exception thrown in onEach{}")
    }.collect { }
    */
}