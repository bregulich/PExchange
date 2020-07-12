package com.bokugan.pexchange.web

import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import com.squareup.moshi.Json
import java.lang.Exception

data class CurrencyPairWeb(
    @field:Json(name = "ccy") val baseCurrency: String,
    // Api has some misconceptions about base and quote currency.
    @field:Json(name = "base_ccy") val quoteCurrency: String,
    @field:Json(name = "buy") val buy: Double,
    @field:Json(name = "sale") val sell: Double
)

fun CurrencyPairWeb.toHistoricalCurrencyPair(timeCreatedUTC: Long) =
    HistoricalCurrencyPair(
        baseCurrency.toCurrency(),
        quoteCurrency.toCurrency(),
        buy,
        sell,
        timeCreatedUTC
    )

private fun String.toCurrency() = when (this) {
    "UAH" -> Currency.UAH
    "USD" -> Currency.USD
    "EUR" -> Currency.EUR
    "RUR" -> Currency.RUB
    else -> throw Exception("Can't parse currency from string: $this")
}