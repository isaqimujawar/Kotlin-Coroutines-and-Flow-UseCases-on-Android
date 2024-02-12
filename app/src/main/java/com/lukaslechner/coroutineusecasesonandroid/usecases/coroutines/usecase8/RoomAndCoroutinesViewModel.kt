package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch

class RoomAndCoroutinesViewModel(
    private val api: MockApi,
    private val database: AndroidVersionDao
) : BaseViewModel<UiState>() {

    fun loadData() {
        // Step 1: Load Data from the Database
        // Step 1a: Show loading indicator for LoadFromDb
        // Step 1b: Use launch{} coroutine builder to call the Dao suspend function.
        uiState.value = UiState.Loading.LoadFromDb
        viewModelScope.launch {
            val localVersions = database.getAndroidVersions()       // suspension point - coroutine is suspended/paused and resumes once suspend function completes

            // Step 2: Check if there are actually any Android versions stored in the database.
            if (localVersions.isEmpty()) {
                uiState.value = UiState.Error(DataSource.DATABASE, "Database empty!")
            } else {
                uiState.value = UiState.Success(DataSource.DATABASE, localVersions.mapToUiModelList())
            }

            try {
                // Step 3: Perform Network Request
                // Step 3a: Show loading indicator for LoadFromNetwork
                // Step 3b: Using the  suspend function, make an API call to get the list of recent versions.
                uiState.value = UiState.Loading.LoadFromNetwork
                val recentVersions: List<AndroidVersion> = api.getRecentAndroidVersions()

                // Step 4: Insert each version in RoomDB
                for (version in recentVersions) {
                    database.insert(version.mapToEntity())
                }

                // Step 5: Also Update the UI with the data received from the Network
                uiState.value = UiState.Success(DataSource.NETWORK, recentVersions)

            } catch (exception: Exception) {
                uiState.value = UiState.Error(DataSource.NETWORK, "Something went wrong!")
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            database.clear()
        }
    }
}

enum class DataSource(val dataSourceName: String) {
    DATABASE("Database"),
    NETWORK("Network")
}