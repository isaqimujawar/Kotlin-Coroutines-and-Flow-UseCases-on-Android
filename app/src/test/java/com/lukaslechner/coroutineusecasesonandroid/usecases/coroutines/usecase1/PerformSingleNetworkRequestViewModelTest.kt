package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class PerformSingleNetworkRequestViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val receivedUiStates = mutableListOf<UiState>()

    private fun observeViewModel(viewModel: PerformSingleNetworkRequestViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null)
                receivedUiStates.add(uiState)
        }
    }


    @Test
    fun `should return Success when network request is successful`() = runTest {

        // Arrange

        val dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        val fakeApi = FakeSuccessApi()
        val viewModel = PerformSingleNetworkRequestViewModel(fakeApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performSingleNetworkRequest()

        // Assert
        assertEquals(
            listOf(UiState.Loading, UiState.Success(mockAndroidVersions)),
            receivedUiStates
            )

        Dispatchers.resetMain()
    }

    @Test
    fun `should return Success when network request is successful using mockk`() = runTest {
        // Arrange
        val mockApi = mockk<MockApi>()
        coEvery { mockApi.getRecentAndroidVersions() } returns mockAndroidVersions

        val viewModel = PerformSingleNetworkRequestViewModel(mockApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performSingleNetworkRequest()

        // Assert
        assertEquals(
            listOf(UiState.Loading, UiState.Success(mockAndroidVersions)),
            receivedUiStates
        )

        // verify
        coVerify(exactly = 1) {
            mockApi.getRecentAndroidVersions()
        }
    }

    @Test
    fun `should return Error when network request fails`() = runTest {
        // Arrange
        val fakeApi = FakeErrorApi()
        val viewModel = PerformSingleNetworkRequestViewModel(fakeApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performSingleNetworkRequest()

        // Assert
        assertEquals(
            listOf(UiState.Loading, UiState.Error("Network request failed!")),
            receivedUiStates
        )
    }

    @Test
    fun `should return Error when network request fails with mockk`() = runTest {
        // Arrange
        val mockApi = mockk<MockApi>()
        coEvery { mockApi.getRecentAndroidVersions() } answers {
            throw HttpException(
                Response.error<List<AndroidVersion>>(
                    500,
                    ResponseBody.create(MediaType.parse("application/json"), "")
                )
            )
        }

        val viewModel = PerformSingleNetworkRequestViewModel(mockApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performSingleNetworkRequest()

        // Assert
        assertEquals(
            listOf(UiState.Loading, UiState.Error("Network request failed!")),
            receivedUiStates
        )

        // verify
        coVerify(exactly = 1) {
            mockApi.getRecentAndroidVersions()
        }
    }
}