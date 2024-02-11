package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
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
                retry(numberOfRetries) {
                    loadRecentAndroidVersions()
                }
            } catch (exception: Exception) {
                Timber.tag("Network request failed").e(exception)
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }

    private suspend fun <T> retry(numberOfRetries: Int, block: suspend () -> T): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (exception: Exception) {        // first two exceptions are caught here
                Timber.tag("retry()").e(exception)
            }
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