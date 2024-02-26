package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Stock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

class FlowUseCase2ViewModel(
    stockPriceDataSource: StockPriceDataSource,
    defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<UiState>() {

    /*

    Flow exercise 1 Goals
        1) only update stock list when Alphabet(Google) (stock.name ="Alphabet (Google)") stock price is > 2300$
        2) only show stocks of "United States" (stock.country == "United States")
        3) show the correct rank (stock.rank) within "United States", not the world wide rank
        4) filter out Apple  (stock.name ="Apple") and Microsoft (stock.name ="Microsoft"), so that Google is number one
        5) only show company if it is one of the biggest 10 companies of the "United States" (stock.rank <= 10)
        6) stop flow collection after 10 emissions from the dataSource
        7) log out the number of the current emission so that we can check if flow collection stops after exactly 10 emissions
        8) Perform all flow processing on a background thread

     */

    val currentStockPriceAsLiveData: MutableLiveData<UiState> = MutableLiveData()

    init {
        val list = mutableListOf<Stock>()
        viewModelScope.launch {
            stockPriceDataSource
                .latestStockList
                .take(10)
                .onCompletion {
                    Timber.d("Flow cancelled")
                }
                .collect {
                    Timber.d("collecting from flow")
                    list.addAll(it)
                    val usList = list
                        .filter { it.country == "United States" }
                        .filter { it.name != "Apple" }
                        .filter { it.name != "Microsoft" }
                        .take(10)
                    currentStockPriceAsLiveData.value = UiState.Success(usList)
                }
        }

    }
}