package com.bokugan.pexchange.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bokugan.pexchange.entities.Currency
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface CurrencyPairDao {

    @Insert
    fun addItems(items: List<CurrencyPairDBItem>): Completable

    @Query(
        """
            SELECT * FROM currency_pairs 
            WHERE base_currency == :baseCurrency 
            AND quote_currency == :quoteCurrency
            ORDER BY created_utc
            LIMIT 1
        """
    )
    fun getLatestItem(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<CurrencyPairDBItem>

    @Query(
        """
            SELECT * FROM currency_pairs
            WHERE base_currency == :baseCurrency 
            AND quote_currency == :quoteCurrency
            ORDER BY created_utc
        """
    )
    fun getItemsInHistoricalOrder(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<List<CurrencyPairDBItem>>
}