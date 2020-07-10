package com.bokugan.pexchange.interfaceadapters.repositories

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.Empty

import com.bokugan.pexchange.usecases.Success

import com.bokugan.pexchange.usecases.boundaries.CurrencySource

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val remoteDataSource: RemoteCurrencyDataSource,
    private val localDataSource: LocalCurrencyDataSource
) : CurrencySource {

    override fun getCurrencyHistory(baseCurrency: Currency, quoteCurrency: Currency) =
        remoteDataSource
            .fetch()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                if (it is Success) localDataSource.addItems(it.data)
            }
            .onErrorResumeNext { Single.just(Empty) }
            .flatMapObservable {
                localDataSource.getItemsInHistoricalOrder(baseCurrency, quoteCurrency)
            }

    override fun getCurrencyPair(baseCurrency: Currency, quoteCurrency: Currency) =
        localDataSource.getLatestItem(baseCurrency, quoteCurrency)
}