package com.bokugan.pexchange.web

import com.squareup.moshi.Json

data class CurrencyPairWeb(
    @field:Json(name = "ccy") val baseCurrency: String,
    // Api has some misconceptions about base and quote currency.
    @field:Json(name = "base_ccy") val quoteCurrency: String,
    @field:Json(name = "buy") val buy: Double,
    @field:Json(name = "sale") val sell: Double
)