package com.bokugan.pexchange.entities

data class HistoricalCurrencyPair(
    override val baseCurrency: Currency,
    override val quoteCurrency: Currency,
    override val buy: Double,
    override val sell: Double,
    val longCreatedUTC: Long
) : CurrencyPair(baseCurrency, quoteCurrency, buy, sell)