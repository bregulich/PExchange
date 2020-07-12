package com.bokugan.pexchange.web

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.interfaceadapters.repositories.RemoteCurrencyDataSource
import com.bokugan.pexchange.usecases.Empty
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair

import com.bokugan.pexchange.usecases.Success

import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class RemoteCurrencyDataSourceImpl @Inject constructor(
    private val currencyApi: CurrencyApi
) : RemoteCurrencyDataSource {

    override fun fetch() =
        currencyApi.getCurrencyRates()
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful && it.body() != null) {
                    it.body()
                } else {
                    emptyList()
                }
            }
            .flatMapObservable { it.toObservable() }
            .filter {
                it.baseCurrency.canConvertToCurrency &&
                        it.quoteCurrency.canConvertToCurrency
            }
            .map {
                it.toHistoricalCurrencyPair(
                    Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis
                )
            }
            .toList()
            .map { if (it.isEmpty()) Empty else Success(it) }
}

private fun CurrencyPairWeb.toHistoricalCurrencyPair(timeCreatedUTC: Long) =
    HistoricalCurrencyPair(
        baseCurrency.toCurrency() ?: throw Error("Unexpected parsing error"),
        quoteCurrency.toCurrency() ?: throw Error("Unexpected parsing error"),
        buy,
        sell,
        timeCreatedUTC
    )

private fun String.toCurrency() = when (this) {
    "UAH" -> Currency.UAH
    "USD" -> Currency.USD
    "EUR" -> Currency.EUR
    "RUR" -> Currency.RUB
    else -> null
}

private val String.canConvertToCurrency get() = toCurrency() != null