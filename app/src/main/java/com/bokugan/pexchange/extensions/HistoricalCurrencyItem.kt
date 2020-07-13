package com.bokugan.pexchange.extensions

import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import java.text.DateFormat
import java.util.*

private val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)

val HistoricalCurrencyPair.localDateTime: String
    get() =
        formatter.format(Date(createdUTC))

val HistoricalCurrencyPair.baseQuote
    get() =
        "$baseCurrency/$quoteCurrency"

val HistoricalCurrencyPair.buySell
    get() =
        "${buy.round(4)}/${sell.round(4)}"