package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.HttpException
import retrofit2.Response

class Perform2SequentialNetworkRequestsViewModelTest {

    // Arrange
    // Act
    // Assert

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val receivedUiStates = mutableListOf<UiState>()
    private fun observeViewModel(viewModel: BaseViewModel<UiState>) {
        viewModel.uiState().observeForever { uiStates ->
            if (uiStates != null) receivedUiStates.add(uiStates)
        }
    }

    @Test
    fun `should return Success when both network request is successful`() {
        // Arrange
        val mockApi = mockk<MockApi>()
        coEvery { mockApi.getRecentAndroidVersions() } returns mockAndroidVersions
        coEvery { mockApi.getAndroidVersionFeatures(29) } returns mockVersionFeaturesAndroid10
        val viewModel = Perform2SequentialNetworkRequestsViewModel(mockApi)
        observeViewModel(viewModel)

        // Act
        viewModel.perform2SequentialNetworkRequest()

        // Assert
        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockVersionFeaturesAndroid10)),
            receivedUiStates)
    }

    @Test
    fun `should return Error when network request fails for recent versions`() {
        // Arrange
        val mockApi = mockk<MockApi>()
        val viewModel = Perform2SequentialNetworkRequestsViewModel(mockApi)
        observeViewModel(viewModel)

        coEvery { mockApi.getRecentAndroidVersions() } answers {
            throw HttpException(
                Response.error<List<AndroidVersion>>(
                    500,
                    ResponseBody.create(MediaType.parse("application/json"), "")
                )
            )
        }

        // Act
        viewModel.perform2SequentialNetworkRequest()

        // Assert
        assertEquals(
            listOf(UiState.Loading, UiState.Error("Network request failed!")),
            receivedUiStates
            )
    }

    @Test
    fun `should return Error when network request fails for version features`() {
        // Arrange
        val mockApi = mockk<MockApi>()
        val viewModel = Perform2SequentialNetworkRequestsViewModel(mockApi)
        observeViewModel(viewModel)

        coEvery { mockApi.getAndroidVersionFeatures(29) } answers {
            throw HttpException(
                Response.error<List<AndroidVersion>>(
                    500,
                    ResponseBody.create(MediaType.parse("application/json"), "")
                )
            )
        }
        // Act
        viewModel.perform2SequentialNetworkRequest()

        // Assert
        assertEquals(
            listOf(UiState.Loading, UiState.Error("Network request failed!")),
            receivedUiStates
        )
    }
}