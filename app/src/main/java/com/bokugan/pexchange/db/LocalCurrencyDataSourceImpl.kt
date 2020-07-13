package com.bokugan.pexchange.db

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.entities.CurrencyPair
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

// TODO. This might not be a correct place for such logic.
//  It is supposed to be somewhere in the repository or even the use case layer.
//  It is quite useful here though: we have a very clean repo api for use case this way.
//  On the other hand, there's quite of business/app logic leaking to the framework layer.
//  So i'm still on the fence...
private fun HistoricalCurrencyPair.invertIfNecessary(
    baseCurrency: Currency,
    quoteCurrency: Currency
) =
    if (this.baseCurrency == baseCurrency && this.quoteCurrency == quoteCurrency) {
        this
    } else {
        createInverted().toHistoricalCurrencyPair(createdUTC)
    }

// TODO. Override in HistoricalCurrencyPair. No? Yes?
private fun CurrencyPair.toHistoricalCurrencyPair(createdUtc: Long) = HistoricalCurrencyPair(
    baseCurrency,
    quoteCurrency,
    buy,
    sell,
    createdUtc
)

private fun HistoricalCurrencyPair.toCurrencyPairDBItem() = CurrencyPairDBItem(
    baseCurrency = baseCurrency,
    quoteCurrency = quoteCurrency,
    buy = buy,
    sell = sell,
    createdUTC = createdUTC
)

private fun CurrencyPairDBItem.toHistoricalCurrencyPair() = HistoricalCurrencyPair(
    baseCurrency,
    quoteCurrency,
    buy,
    sell,
    createdUTC
)