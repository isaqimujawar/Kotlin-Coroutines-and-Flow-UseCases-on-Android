package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class FlowUseCase1ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsLiveData: MutableLiveData<UiState> = MutableLiveData()

    init {
        stockPriceDataSource
            .latestStockList
            .onStart {
                Timber.d("The Flow starts to be collected 1")
            }
            .onCompletion { cause ->
                Timber.d("Flow has completed")
                Timber.d("cause is ${cause?.message}")
            }
            .onEach { stockList ->
                currentStockPriceAsLiveData.value = UiState.Success(stockList)
            }
            .onStart {
                Timber.d("The Flow starts to be collected 2")
            }
            .launchIn(viewModelScope)
    }
}