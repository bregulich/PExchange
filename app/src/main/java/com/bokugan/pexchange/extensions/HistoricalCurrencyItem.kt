package com.bokugan.pexchange.extensions

import com.bokugan.pexchange.db.CurrencyPairDBItem
import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.entities.CurrencyPair
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

// TODO. Cross currency.
// TODO. This might not be a correct place for such logic.
//  It is supposed to be somewhere in the repository or even the use case layer.
//  It is quite useful here though: we have a very clean repo api for use case this way.
//  On the other hand, there's quite of business/app logic leaking to the framework layer.
//  So i'm still on the fence...
fun HistoricalCurrencyPair.invertIfNecessary(
    baseCurrency: Currency,
    quoteCurrency: Currency
) =
    if (this.baseCurrency == baseCurrency && this.quoteCurrency == quoteCurrency) {
        this
    } else {
        createInverted().toHistoricalCurrencyPair(createdUTC)
    }

fun HistoricalCurrencyPair.toCurrencyPairDBItem() = CurrencyPairDBItem(
    baseCurrency = baseCurrency,
    quoteCurrency = quoteCurrency,
    buy = buy,
    sell = sell,
    createdUTC = createdUTC
)

// TODO. Override in HistoricalCurrencyPair. No? Yes?
fun CurrencyPair.toHistoricalCurrencyPair(createdUtc: Long) = HistoricalCurrencyPair(
    baseCurrency,
    quoteCurrency,
    buy,
    sell,
    createdUtc
)
