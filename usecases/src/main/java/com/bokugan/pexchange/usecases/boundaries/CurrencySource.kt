package com.bokugan.pexchange.usecases.boundaries

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.entities.CurrencyPair
import com.bokugan.pexchange.entities.HistoricalCurrencyPair
import io.reactivex.Observable

interface CurrencySource {

    fun getCurrencyHistory(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<List<HistoricalCurrencyPair>>

    fun getCurrencyPair(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<CurrencyPair>
}