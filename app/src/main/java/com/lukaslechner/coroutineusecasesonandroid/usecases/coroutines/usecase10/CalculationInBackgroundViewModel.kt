package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInBackgroundViewModel : BaseViewModel<UiState>() {

    /**
     *  Using withContext() suspend function
     *      - to modify the CoroutineContext Elements
     *      - and switch to Dispatchers.Default, which runs the coroutine on the background thread.
     *      - We use Dispatchers.Default for very CPU intensive tasks.
     *      - Also we can change the name of the Coroutine
     */
    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading

        // Performing the calculation in a coroutine is by default on the main thread
        viewModelScope.launch(CoroutineName("Coroutine started with launch")) {
            Timber.d("Coroutine Context: $coroutineContext")

            var result = BigInteger.ONE
            val computationDuration = measureTimeMillis {
                result = calculateFactorialOf(factorialOf)
            }

            var resultString = ""
            val stringConversionDuration = measureTimeMillis {
                resultString = withContext(
                    Dispatchers.Default + CoroutineName("String Conversion Coroutine")
                ) {
                    Timber.d("Coroutine Context: $coroutineContext")
                    result.toString()
                }
            }

            uiState.value = UiState.Success(resultString, computationDuration, stringConversionDuration)
        }
    }

    private suspend fun calculateFactorialOf(number: Int) =
        withContext(context = Dispatchers.Default + CoroutineName("Calculate Factorial Coroutine")) {
            Timber.d("Coroutine Context: $coroutineContext")
            var factorial = BigInteger.ONE
            for (i in 1..number) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            Timber.d("Calculating Factorial Completed")
            factorial
        }
}