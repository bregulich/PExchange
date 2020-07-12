package com.bokugan.pexchange.ui.convertcurrency

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.ConvertCurrency
import com.bokugan.pexchange.usecases.Success
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private const val DEBOUNCE = 500L

private data class ConvertCurrencyRequest(
    val baseCurrency: Currency,
    val quoteCurrency: Currency,
    val amount: Double
)

class ConvertCurrencyViewModel @ViewModelInject constructor(
    private val convertCurrency: ConvertCurrency
) : ViewModel() {

    private val _quoteAmount = MutableLiveData(0.0)
    val quoteAmount: LiveData<Double> = _quoteAmount

    val baseCurrency = MutableLiveData(Currency.USD)
    val quoteCurrency = MutableLiveData(Currency.UAH)
    val amount = MutableLiveData(0.0)

    private val mediator = MediatorLiveData<Nothing>()
        .apply {
            addSource(baseCurrency) { requestCurrencyConversion() }
            addSource(quoteCurrency) { requestCurrencyConversion() }
            addSource(amount) { requestCurrencyConversion() }
        }

    private val convertCurrencySubject =
        PublishSubject.create<ConvertCurrencyRequest>()

    private val compositeDisposable = CompositeDisposable().also {
        it += createCurrencyConversionStream()
    }

    private fun createCurrencyConversionStream() =
        convertCurrencySubject
            .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
            .switchMap { (bccy, qcyy, amount) ->
                convertCurrency(bccy, qcyy, amount)
            }
            .subscribe {
                _quoteAmount.postValue(
                    if (it is Success) it.data else 0.0
                )
            }

    private fun requestCurrencyConversion() {
        convertCurrencySubject.onNext(
            ConvertCurrencyRequest(
                baseCurrency.value!!,
                quoteCurrency.value!!,
                amount.value!!
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}