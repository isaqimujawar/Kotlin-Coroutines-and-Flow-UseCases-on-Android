package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import timber.log.Timber
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CooperativeCancellationViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private val uiState: MutableLiveData<UiState> = MutableLiveData()

    fun uiState(): LiveData<UiState> {
        return uiState
    }

    private var calculatingJob: Job? = null

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading

        calculatingJob = viewModelScope.launch(CoroutineName("Coroutine started with launch")) {

            var result = BigInteger.ONE
            val computationDuration = measureTimeMillis {
                result = calculateFactorialOf(factorialOf)
            }

            var resultString = ""
            val stringConversionDuration = measureTimeMillis {
                resultString = withContext(defaultDispatcher) { result.toString() }
            }

            uiState.value = UiState.Success(resultString, computationDuration, stringConversionDuration)
        }

        calculatingJob?.invokeOnCompletion {
            if (it is CancellationException)
                Timber.d("Calculation was cancelled")
        }
    }

    fun cancelCalculation() {
        Timber.d("cancelCalculation() method was called")
        calculatingJob?.cancel()
    }

    private suspend fun calculateFactorialOf(number: Int) =
        withContext(defaultDispatcher) {
            var factorial = BigInteger.ONE
            for (i in 1..number) {
                yield()         // checks if the Coroutine of the Job is still Active or not
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            Timber.d("Calculating Factorial Completed")
            factorial
        }
}