package com.bokugan.pexchange.db

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.interfaceadapters.repositories.LocalCurrencyDataSource
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import com.bokugan.pexchange.usecases.Result
import io.reactivex.Observable
import javax.inject.Inject

class LocalCurrencyDataSourceImpl @Inject constructor(

) : LocalCurrencyDataSource {

    override fun addItems(items: List<HistoricalCurrencyPair>) {
        TODO("Not yet implemented")
    }

    override fun getLatestItem(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<Result<HistoricalCurrencyPair>> {
        TODO("Not yet implemented")
    }

    override fun getItemsInHistoricalOrder(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<Result<List<HistoricalCurrencyPair>>> {
        TODO("Not yet implemented")
    }
}