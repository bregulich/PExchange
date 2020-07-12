package com.bokugan.pexchange.interfaceadapters.repositories

import com.bokugan.pexchange.usecases.Result as Result

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import io.reactivex.Completable
import io.reactivex.Observable

interface LocalCurrencyDataSource {

    fun addItems(items: List<HistoricalCurrencyPair>): Completable

    fun getLatestItem(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<HistoricalCurrencyPair>>

    fun getItemsInHistoricalOrder(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<List<HistoricalCurrencyPair>>>
}