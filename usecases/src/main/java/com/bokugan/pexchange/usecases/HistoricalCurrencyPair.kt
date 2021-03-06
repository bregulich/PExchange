package com.bokugan.pexchange.usecases

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.entities.CurrencyPair

data class HistoricalCurrencyPair(
    override val baseCurrency: Currency,
    override val quoteCurrency: Currency,
    override val buy: Double,
    override val sell: Double,
    val createdUTC: Long
) : CurrencyPair(baseCurrency, quoteCurrency, buy, sell)