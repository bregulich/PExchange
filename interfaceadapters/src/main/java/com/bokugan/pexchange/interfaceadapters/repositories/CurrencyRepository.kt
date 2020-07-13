package com.bokugan.pexchange.interfaceadapters.repositories

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import com.bokugan.pexchange.usecases.Result

import com.bokugan.pexchange.usecases.Success

import com.bokugan.pexchange.usecases.boundaries.CurrencySource
import io.reactivex.Completable
import io.reactivex.Observable

import javax.inject.Inject

interface Updatable {
    fun update(): Completable
}

interface UpdateDelegate {
    fun delegateUpdates(updatable: Updatable): Completable
}

class CurrencyRepository @Inject constructor(
    private val remoteDataSource: RemoteCurrencyDataSource,
    private val localDataSource: LocalCurrencyDataSource,
    updater: UpdateDelegate
) : CurrencySource, Updatable {

    init {
        updater.delegateUpdates(this)
    }

    override fun update() =
        remoteDataSource
            .fetch()
            .flatMapCompletable {
                if (it is Success) {
                    localDataSource.addItems(it.data)
                } else {
                    Completable.complete()
                }
            }
            .onErrorResumeNext { Completable.complete() }

    override fun getCurrencyHistory(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<List<HistoricalCurrencyPair>>> =
        update()
            .andThen(localDataSource.getItemsInHistoricalOrder(baseCurrency, quoteCurrency))

    override fun getCurrencyPair(baseCurrency: Currency, quoteCurrency: Currency) =
        localDataSource.getLatestItem(baseCurrency, quoteCurrency)
}