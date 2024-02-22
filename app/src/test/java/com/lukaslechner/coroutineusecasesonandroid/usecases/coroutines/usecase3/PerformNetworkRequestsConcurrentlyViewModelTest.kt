package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesOreo
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesPie
import com.lukaslechner.coroutineusecasesonandroid.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class PerformNetworkRequestsConcurrentlyViewModelTest {

    @get:Rule
    val instantTaskExecutor: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    private fun observeViewModel(viewModel: BaseViewModel<UiState>) {
        viewModel.uiState().observeForever {
            if (it != null) receivedUiStates.add(it)
        }
    }

    private val expected: List<Any> = listOf(
        UiState.Loading,
        UiState.Success(
            listOf(
                mockVersionFeaturesOreo,
                mockVersionFeaturesPie,
                mockVersionFeaturesAndroid10
            )
        )
    )

    @Test
    fun `performNetworkRequestSequentially() should load data sequentially`() = runTest {
        // Arrange
        val mockApi = mockk<MockApi>()
        val viewModel = PerformNetworkRequestsConcurrentlyViewModel(mockApi)
        observeViewModel(viewModel)

        coEvery { mockApi.getRecentAndroidVersions() } returns mockAndroidVersions
        coEvery { mockApi.getAndroidVersionFeatures(27) } answers { mockVersionFeaturesOreo }
        coEvery { mockApi.getAndroidVersionFeatures(28) } returns mockVersionFeaturesPie
        coEvery { mockApi.getAndroidVersionFeatures(29) } returns mockVersionFeaturesAndroid10

        // Act
        viewModel.performNetworkRequestsSequentially()
        advanceUntilIdle()

        // Assert
        Assert.assertEquals(expected, receivedUiStates)
    }

    @Test
    fun `performNetworkRequestConcurrently() should load data concurrently`() = runTest {
        // Arrange
        val mockApi = mockk<MockApi>()
        val viewModel = PerformNetworkRequestsConcurrentlyViewModel(mockApi)
        observeViewModel(viewModel)

        coEvery { mockApi.getRecentAndroidVersions() } returns mockAndroidVersions
        coEvery { mockApi.getAndroidVersionFeatures(27) } returns mockVersionFeaturesOreo
        coEvery { mockApi.getAndroidVersionFeatures(28) } returns mockVersionFeaturesPie
        coEvery { mockApi.getAndroidVersionFeatures(29) } returns mockVersionFeaturesAndroid10

        // Act
        viewModel.performNetworkRequestsConcurrently()
        advanceUntilIdle()

        // Assert
        Assert.assertEquals(expected, receivedUiStates)
    }
}