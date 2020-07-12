package com.bokugan.pexchange.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CurrencyPairDBItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {
    abstract fun currencyPairDao(): CurrencyPairDao
}