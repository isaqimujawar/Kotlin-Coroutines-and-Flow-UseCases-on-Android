package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.launch

class ContinueCoroutineWhenUserLeavesScreenViewModel(
    private var repository: AndroidVersionRepository
) : BaseViewModel<UiState>() {

    // more information in this blogpost about "Coroutines & Patterns for work that shouldn't
    // be cancelled" =>
    // https://medium.com/androiddevelopers/coroutines-patterns-for-work-that-shouldnt-be-cancelled-e26c40f142ad

    fun loadData() {
        // Step 1 - Show Loading Indicator for LoadFromDb
        uiState.value = UiState.Loading.LoadFromDb

        // Step 2 - Launch a New Coroutine in the viewModelScope
        viewModelScope.launch {
            // Step 3 - Get Data from the Repository
            val localVersions = repository.getLocalAndroidVersions()

            // Step 4 - Check if Data returned from the Repository is Empty
            // Step 4a - if Data isNotEmpty, then show UiState.Success(localData)
            if (localVersions.isNotEmpty()) {
                uiState.value =
                    UiState.Success(DataSource.Database, localVersions)
            } else {
                // Step 4b - if Data is Empty, then show UiState.Error() toast message
                uiState.value =
                    UiState.Error(DataSource.Database, "Database empty!")
            }

            // Step 5 - In any case, Show Loading Indicator for LoadFromNetwork
            uiState.value = UiState.Loading.LoadFromNetwork

            // Step 6 - If Network Response is Successful, then show UiState.Success(networkData)
            try {
                uiState.value = UiState.Success(
                    DataSource.Network,
                    repository.loadAndStoreRemoteAndroidVersions()
                )
            } catch (exception: Exception) {
                uiState.value = UiState.Error(DataSource.Network, "Network Request failed")
            }
        }
    }

    fun clearDatabase() {
        repository.clearDatabase()
    }
}

sealed class DataSource(val name: String) {
    object Database : DataSource("Database")
    object Network : DataSource("Network")
}