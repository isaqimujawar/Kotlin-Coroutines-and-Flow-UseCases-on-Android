package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

/**
 * Using job to cancel coroutine
 * cancelling the coroutine will stop the Flow request of new stock prices
 */
class FlowUseCase1ViewModel(
    private val stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsLiveData: MutableLiveData<UiState> = MutableLiveData()

    var job: Job? = null

    fun startFlowCollection() {
        job = stockPriceDataSource
            .latestStockList
            .map { stockList -> UiState.Success(stockList) as UiState }
            .onStart { emit(UiState.Loading) }
            .onEach { uiState -> currentStockPriceAsLiveData.value = uiState }
            .onCompletion {
                Timber.tag("Flow").d("Flow has completed. Cause is ${it?.message}")
            }
            .launchIn(viewModelScope)
    }

    fun stopFlowCollection() {
        job?.cancel()
    }
}