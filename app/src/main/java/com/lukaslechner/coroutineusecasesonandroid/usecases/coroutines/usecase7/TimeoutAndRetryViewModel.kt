package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2
        val timeout = 1000L

        // TODO: Exercise 3
        // switch to branch "coroutine_course_full" to see solution

        // run api.getAndroidVersionFeatures(27) and api.getAndroidVersionFeatures(28) in parallel

        val oreoFeaturesDeferred = viewModelScope.async {
            retryWithTimeout(numberOfRetries, timeout) {
                api.getAndroidVersionFeatures(27)
            }
        }
        val pieFeaturesDeferred = viewModelScope.async {
            retryWithTimeout(numberOfRetries, timeout) {
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch {
            try {
                val recentFeatures = listOf(oreoFeaturesDeferred, pieFeaturesDeferred).awaitAll()
                uiState.value = UiState.Success(recentFeatures)
            } catch (exception: Exception) {
                Timber.tag("Network request failed").e(exception)
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }

    private suspend fun <T> retryWithTimeout(
     numberOfRetries: Int,
     timeout: Long,
     block: suspend () -> T
    ) = retry(numberOfRetries) {
        withTimeout(timeout) {
            block()
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        delayBetweenRetries: Long = 100,
        block: suspend () -> T
    ): T {
        repeat(numberOfRetries) {
            Timber.tag("RepeatRetry").d("repeat = $it")
            try {
                return block()
            } catch (exception: Exception) {        // first two exceptions are caught here
                Timber.tag("RepeatRetry").e(exception)
            }
            delay(delayBetweenRetries)
        }
        return block()          // this line of code will only be reached/executed, if the return statement inside the try block is not executed.
    }

    /*private suspend fun usingWithTimeout(timeout: Long) =
        withTimeout(timeout) {
            val oreoFeaturesDeferred = viewModelScope.async { api.getAndroidVersionFeatures(27) }
            val pieFeaturesDeferred = viewModelScope.async { api.getAndroidVersionFeatures(28) }

            val recentFeatures = awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred)
            uiState.value = UiState.Success(recentFeatures)
        }*/
}