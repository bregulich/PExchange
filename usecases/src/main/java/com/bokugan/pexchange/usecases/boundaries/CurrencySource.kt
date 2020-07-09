package com.bokugan.pexchange.usecases.boundaries

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.entities.CurrencyPair
import com.bokugan.pexchange.usecases.Result
import io.reactivex.Observable

interface CurrencySource {

    fun getCurrencyHistory(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<List<CurrencyPair>>>

    fun getCurrencyPair(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<out Result<CurrencyPair>>
}