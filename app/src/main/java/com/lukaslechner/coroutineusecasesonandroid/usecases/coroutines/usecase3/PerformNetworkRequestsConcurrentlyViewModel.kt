package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading

        try {
            viewModelScope.launch {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)
            }
        } catch (exception: Exception) {
            uiState.value = UiState.Error("Network request failed")
        }

    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        val oreoFeaturesDeferred = viewModelScope.async { mockApi.getAndroidVersionFeatures(27) }

        val pieFeaturesDeferred = viewModelScope.async { mockApi.getAndroidVersionFeatures(28) }

        val android10FeaturesDeferred = viewModelScope.async { mockApi.getAndroidVersionFeatures(29) }

        viewModelScope.launch {
            // Every suspend function can be a suspension point.
            // And can suspend/pause the coroutine.
            // So, at each await() call, the coroutine started with launch{} is suspended
            // and is resumed as soon as the await() is completed.
            try {
//                val oreoFeatures = oreoFeaturesDeferred.await()     // 1st suspension point inside launch{}
//                val pieFeatures = pieFeaturesDeferred.await()       // 2nd suspension point
//                val android10Features = android10FeaturesDeferred.await()   // last suspension point
//                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)

                val versionFeatures =
                    awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)

                uiState.value = UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network request failed")
            }
        }
    }
}