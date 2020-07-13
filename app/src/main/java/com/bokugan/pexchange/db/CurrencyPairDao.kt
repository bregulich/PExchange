package com.bokugan.pexchange.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.extensions.invertIfNecessary
import io.reactivex.Observable

@Dao
interface CurrencyPairDao {

    @Transaction
    fun addItems(items: List<CurrencyPairDBItem>) {
        for (newItem in items) {
            with(newItem) {
                val dbItem =
                    getLatestItemSync(baseCurrency, quoteCurrency)
                        ?.toHistoricalCurrencyPair()
                        ?.invertIfNecessary(baseCurrency, quoteCurrency)

                if (dbItem == null || buy != dbItem.buy || sell != dbItem.sell) {
                    addItemSync(newItem)
                }
            }
        }
    }

    @Insert
    fun addItemSync(item: CurrencyPairDBItem)

    @Query(
        """
            SELECT * FROM currency_pairs 
            WHERE 
                    (base_currency == :baseCurrency AND quote_currency == :quoteCurrency) 
                OR 
                    (base_currency == :quoteCurrency AND quote_currency == :baseCurrency)
            ORDER BY created_utc
            LIMIT 1
        """
    )
    fun getLatestItemSync(baseCurrency: Currency, quoteCurrency: Currency): CurrencyPairDBItem?

    @Query(
        """
            SELECT * FROM currency_pairs 
            WHERE 
                    (base_currency == :baseCurrency AND quote_currency == :quoteCurrency) 
                OR 
                    (base_currency == :quoteCurrency AND quote_currency == :baseCurrency)
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
            WHERE 
                    (base_currency == :baseCurrency AND quote_currency == :quoteCurrency) 
                OR 
                    (base_currency == :quoteCurrency AND quote_currency == :baseCurrency) 
            ORDER BY created_utc DESC
        """
    )
    fun getItemsInHistoricalOrder(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Observable<List<CurrencyPairDBItem>>
}