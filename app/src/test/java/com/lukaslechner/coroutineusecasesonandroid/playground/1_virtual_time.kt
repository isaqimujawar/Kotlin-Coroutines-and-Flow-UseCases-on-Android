package com.lukaslechner.coroutineusecasesonandroid.playground

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class SystemUnderTest {

    suspend fun functionWithDelay(): Int {
        delay(1000)
        return 42
    }
}

class TestClass {
    @Test
    fun `functionWithDelay() should return 42`() = runBlockingTest {
        val realTimeStart = System.currentTimeMillis()
        val virtualTimeStart = currentTime

        // Arrange
        val sut = SystemUnderTest()

        // Act
        var actual = sut.functionWithDelay()

        // Assert
        Assert.assertEquals(42, actual)

        val realTimeDuration = System.currentTimeMillis() - realTimeStart
        val virtualTimeDuration = currentTime - virtualTimeStart
        println("Test took $realTimeDuration real ms")
        println("Test took $virtualTimeDuration virtual ms")
    }

    @Test
    fun `illustrate the advanceTimeBy() function`() = runBlockingTest {
        val realTimeStart = System.currentTimeMillis()
        val virtualTimeStart = currentTime

        functionThatStartsNewCoroutine()
        //advanceTimeBy(1000)       // This method is deprecated
        advanceUntilIdle()

        val realTimeDuration = System.currentTimeMillis() - realTimeStart
        val virtualTimeDuration = currentTime - virtualTimeStart
        println("Test took $realTimeDuration real ms")
        println("Test took $virtualTimeDuration virtual ms")
    }
}

fun CoroutineScope.functionThatStartsNewCoroutine() {
    launch {
        delay(1000)
        println("Coroutine Completed")
    }
}