package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {
    var getAndroidVersionsCall: Call<List<AndroidVersion>>? = null
    var getAndroidFeaturesCall: Call<VersionFeatures>? = null

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        getAndroidVersionsCall = mockApi.getRecentAndroidVersions()
        getAndroidVersionsCall!!.enqueue(object: Callback<List<AndroidVersion>>{
            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error("Something unexpected happened!")
            }

            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                when {
                    response.isSuccessful -> {
                        val mostRecentAndroidVersion: AndroidVersion = response.body()!!.last()

                        getAndroidFeaturesCall = mockApi.getAndroidVersionFeatures(mostRecentAndroidVersion.apiLevel)
                        getAndroidFeaturesCall!!.enqueue(object: Callback<VersionFeatures> {
                            override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                                uiState.value = UiState.Error("Something unexpected happened!")
                            }

                            override fun onResponse(
                                call: Call<VersionFeatures>,
                                response: Response<VersionFeatures>
                            ) {
                                when {
                                    response.isSuccessful -> {
                                        val featuresOfMostRecentVersion = response.body()!!
                                        uiState.value = UiState.Success(featuresOfMostRecentVersion)
                                    }
                                    else -> uiState.value = UiState.Error("Network request failed")
                                }
                            }

                        })
                    }
                    else -> uiState.value = UiState.Error("Network request failed")
                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()

        // We have to cancel the Calls to avoid memory leaks
        getAndroidVersionsCall?.cancel()
        getAndroidFeaturesCall?.cancel()
    }
}