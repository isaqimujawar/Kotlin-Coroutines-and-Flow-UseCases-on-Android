package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2

        viewModelScope.launch {

            try {
                retry(numberOfRetries, 400) {
                    loadRecentAndroidVersions()
                }
            } catch (exception: Exception) {
                Timber.tag("Network request failed").e(exception)
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        initialDelayMillis: Long = 100,
        maxDelayMillis: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (exception: Exception) {        // first two exceptions are caught here
                Timber.tag("retry()").e(exception)
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
            Timber.d("currentDelay = $currentDelay")
        }
        return block()          // this line of code will only be reached/executed, if the return statement inside the try block is not executed.
    }

    private suspend fun loadRecentAndroidVersions() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }
}

/*fun performNetworkRequest() {
    uiState.value = UiState.Loading
    val numberOfRetries = 2

    viewModelScope.launch {

        try {
            repeat(times = numberOfRetries) {
                try {
                    loadRecentAndroidVersions()
                    return@launch
                } catch (exception: Exception) {
                    Timber.tag("inside repeat").e(exception)
                }
            }
            loadRecentAndroidVersions()
        } catch (exception: Exception) {
            Timber.tag("Network request failed").e(exception)
            uiState.value = UiState.Error("Network request failed!")
        }
    }
}*/