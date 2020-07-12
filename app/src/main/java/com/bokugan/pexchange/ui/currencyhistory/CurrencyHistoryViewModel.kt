package com.bokugan.pexchange.ui.currencyhistory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private const val DEBOUNCE = 500L

private data class CurrencyHistoryRequest(
    val baseCurrency: Currency,
    val quoteCurrency: Currency
)

class CurrencyHistoryViewModel @ViewModelInject constructor(
    private val getCurrencyHistory: GetCurrencyHistory
) : ViewModel() {

    private val _currencyHistory = MutableLiveData<List<HistoricalCurrencyPair>>(emptyList())
    val currencyHistory: LiveData<List<HistoricalCurrencyPair>> = _currencyHistory

    // TODO. Should be set by the use case, but i'm too lazy.
    val latestCurrencyPair = Transformations.map(currencyHistory) { it?.firstOrNull() }

    private val currencyHistoryRequestSubject =
        PublishSubject.create<CurrencyHistoryRequest>()

    private val compositeDisposable = CompositeDisposable().also {
        it += createCurrencyHistoryUpdateStream()
    }

    private fun createCurrencyHistoryUpdateStream() =
        currencyHistoryRequestSubject
            .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
            .switchMap { (bccy, qccy) -> getCurrencyHistory(bccy, qccy) }
            .subscribe {
                _currencyHistory.postValue(
                    if (it is Success) it.data else emptyList()
                )
            }

    private var baseCurrency = Currency.USD
    private var quoteCurrency = Currency.UAH

    private fun updateCurrencyHistory(
        baseCurrency: Currency? = null,
        quoteCurrency: Currency? = null
    ) {
        this.baseCurrency = baseCurrency ?: this.baseCurrency
        this.quoteCurrency = quoteCurrency ?: this.quoteCurrency

        currencyHistoryRequestSubject.onNext(
            CurrencyHistoryRequest(this.baseCurrency, this.quoteCurrency)
        )
    }

    init {
        updateCurrencyHistory()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}