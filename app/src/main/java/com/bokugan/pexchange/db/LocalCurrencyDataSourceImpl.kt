package com.bokugan.pexchange.db

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.extensions.invertIfNecessary
import com.bokugan.pexchange.extensions.toCurrencyPairDBItem
import com.bokugan.pexchange.interfaceadapters.repositories.LocalCurrencyDataSource
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import com.bokugan.pexchange.usecases.Result
import com.bokugan.pexchange.usecases.Success
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalCurrencyDataSourceImpl @Inject constructor(
    private val currencyPairDao: CurrencyPairDao
) : LocalCurrencyDataSource {

    override fun addItems(items: List<HistoricalCurrencyPair>) =
        items.toObservable()
            .subscribeOn(Schedulers.io())
            .map { it.toCurrencyPairDBItem() }
            .toList()
            .doOnSuccess { currencyPairDao.addItems(it) }
            .flatMapCompletable { Completable.complete() }

    override fun getLatestItem(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<HistoricalCurrencyPair>> =
        currencyPairDao.getLatestItem(baseCurrency, quoteCurrency)
            .subscribeOn(Schedulers.io())
            .map {
                Success(
                    it.toHistoricalCurrencyPair()
                        .invertIfNecessary(baseCurrency, quoteCurrency)
                )
            }

    override fun getItemsInHistoricalOrder(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<List<HistoricalCurrencyPair>>> =
        currencyPairDao.getItemsInHistoricalOrder(baseCurrency, quoteCurrency)
            .subscribeOn(Schedulers.io())
            .map { items ->
                items.map {
                    it.toHistoricalCurrencyPair()
                        .invertIfNecessary(baseCurrency, quoteCurrency)
                }
            }
            .map { Success(it) }
}