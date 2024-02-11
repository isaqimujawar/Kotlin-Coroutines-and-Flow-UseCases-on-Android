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
    }

    private suspend fun loadRecentAndroidVersions() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }
}