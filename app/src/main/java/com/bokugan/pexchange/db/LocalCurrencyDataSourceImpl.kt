package com.bokugan.pexchange.db

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.interfaceadapters.repositories.LocalCurrencyDataSource
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import com.bokugan.pexchange.usecases.Result
import com.bokugan.pexchange.usecases.Success
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalCurrencyDataSourceImpl @Inject constructor(
    private val currencyPairDao: CurrencyPairDao
) : LocalCurrencyDataSource {

    // TODO. Skip logic.
    override fun addItems(items: List<HistoricalCurrencyPair>) =
        items.toObservable()
            .subscribeOn(Schedulers.io())
            .map { it.toCurrencyPairDBItem() }
            .toList()
            .flatMapCompletable { currencyPairDao.addItems(it) }

    override fun getLatestItem(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<HistoricalCurrencyPair>> =
        currencyPairDao.getLatestItem(baseCurrency, quoteCurrency)
            .subscribeOn(Schedulers.io())
            .map { Success(it.toHistoricalCurrencyPair()) }

    override fun getItemsInHistoricalOrder(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<List<HistoricalCurrencyPair>>> =
        currencyPairDao.getItemsInHistoricalOrder(baseCurrency, quoteCurrency)
            .subscribeOn(Schedulers.io())
            .map { items -> items.map { it.toHistoricalCurrencyPair() } }
            .map { Success(it) }
}

private fun HistoricalCurrencyPair.toCurrencyPairDBItem() = CurrencyPairDBItem(
    baseCurrency = baseCurrency,
    quoteCurrency = quoteCurrency,
    buy = buy,
    sell = sell,
    createdUTC = createdUTC
)

private fun CurrencyPairDBItem.toHistoricalCurrencyPair() = HistoricalCurrencyPair(
    baseCurrency = baseCurrency,
    quoteCurrency = quoteCurrency,
    buy = buy,
    sell = sell,
    createdUTC = createdUTC
)