package com.bokugan.pexchange.web

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import com.squareup.moshi.Json

data class JsonCurrencyPair(
    @field:Json(name = "ccy") val baseCurrency: Currency,
    // Api has some misconceptions about base and quote currency.
    @field:Json(name = "base_ccy") val quoteCurrency: Currency,
    @field:Json(name = "buy") val buy: Double,
    @field:Json(name = "sale") val sell: Double
)

fun JsonCurrencyPair.toHistoricalCurrencyPair(timeCreatedUTC: Long) =
    HistoricalCurrencyPair(
        this.baseCurrency,
        this.quoteCurrency,
        this.buy,
        this.sell,
        timeCreatedUTC
    )