package com.bokugan.pexchange.web

import com.bokugan.pexchange.interfaceadapters.repositories.RemoteCurrencyDataSource
import com.bokugan.pexchange.usecases.Empty

import com.bokugan.pexchange.usecases.Success

import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
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
            .map {
                it.toHistoricalCurrencyPair(
                    Calendar.getInstance().timeInMillis
                )
            }
            .toList()
            .map { if (it.isEmpty()) Empty else Success(it) }
}