package com.bokugan.pexchange.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair

@Entity(tableName = "currency_pairs", indices = [Index("created_utc")])
data class CurrencyPairDBItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "base_currency")
    val baseCurrency: Currency,

    @ColumnInfo(name = "quote_currency")
    val quoteCurrency: Currency,

    @ColumnInfo(name = "buy_currency")
    val buy: Double,

    @ColumnInfo(name = "sell_currency")
    val sell: Double,

    @ColumnInfo(name = "created_utc")
    val createdUTC: Long
)

fun CurrencyPairDBItem.toHistoricalCurrencyPair() = HistoricalCurrencyPair(
    baseCurrency,
    quoteCurrency,
    buy,
    sell,
    createdUTC
)