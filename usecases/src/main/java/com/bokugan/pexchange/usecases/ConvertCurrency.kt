package com.bokugan.pexchange.usecases

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.boundaries.CurrencySource
import io.reactivex.Observable

import javax.inject.Inject

class ConvertCurrency @Inject constructor(
    private val currencySource: CurrencySource
) {

    operator fun invoke(
        baseCurrency: Currency,
        quoteCurrency: Currency,
        amount: Double
    ): Observable<Result<Double>> =
        currencySource
            .getCurrencyPair(baseCurrency, quoteCurrency)
            .map { Success(it.baseToQuoteCurrency(amount)) }
}