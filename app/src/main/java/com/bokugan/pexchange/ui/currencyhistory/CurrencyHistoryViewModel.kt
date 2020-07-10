package com.bokugan.pexchange.ui.currencyhistory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.*
import io.reactivex.android.schedulers.AndroidSchedulers
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

    private val _currencyHistory = MutableLiveData<List<HistoricalCurrencyPair>>(null)
    val currencyHistory: LiveData<List<HistoricalCurrencyPair>> = _currencyHistory

    val baseCurrency = MutableLiveData(Currency.USD)
    val quoteCurrency = MutableLiveData(Currency.UAH)

    private val mediator = MediatorLiveData<Nothing>()
        .apply {
            addSource(baseCurrency) { updateCurrencyHistory() }
            addSource(quoteCurrency) { updateCurrencyHistory() }
        }

    private val compositeDisposable = CompositeDisposable().also {
        it += createCurrencyHistoryUpdateStream()
    }

    private val currencyHistoryRequestSubject =
        PublishSubject.create<CurrencyHistoryRequest>()

    private fun createCurrencyHistoryUpdateStream() =
        currencyHistoryRequestSubject
            .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
            .switchMap { (bccy, qccy) -> getCurrencyHistory(bccy, qccy) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _currencyHistory.value =
                    if (it is Success) it.data else null
            }

    private fun updateCurrencyHistory() {
        currencyHistoryRequestSubject.onNext(
            CurrencyHistoryRequest(
                baseCurrency.value!!,
                quoteCurrency.value!!
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}