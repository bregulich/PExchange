package com.bokugan.pexchange.interfaceadapters.repositories.datasources

import com.bokugan.pexchange.usecases.Result as Result

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import io.reactivex.Observable

interface LocalCurrencyDataSource {

    fun addItems(items: List<HistoricalCurrencyPair>)

    fun getLatestItem(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<Result<HistoricalCurrencyPair>>

    fun getItemsInHistoricalOrder(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<Result<List<HistoricalCurrencyPair>>>
}