package com.bokugan.pexchange.db

import androidx.room.TypeConverter
import com.bokugan.pexchange.entities.Currency

class TypeConverters {

    @TypeConverter
    fun Currency.toCode() = this.code

    @TypeConverter
    fun Int.toCurrency() = Currency[this]
}