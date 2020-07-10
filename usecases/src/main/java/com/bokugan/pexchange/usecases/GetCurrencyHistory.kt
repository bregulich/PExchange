package com.bokugan.pexchange.usecases

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.boundaries.CurrencySource
import io.reactivex.Observable

import javax.inject.Inject

class GetCurrencyHistory @Inject constructor(
    private val currencySource: CurrencySource
) {
    operator fun invoke(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<List<HistoricalCurrencyPair>>> =
        currencySource.getCurrencyHistory(baseCurrency, quoteCurrency)
}