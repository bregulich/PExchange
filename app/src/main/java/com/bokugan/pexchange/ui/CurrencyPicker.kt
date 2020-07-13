package com.bokugan.pexchange.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import com.bokugan.pexchange.entities.Currency

// TODO. Not cool.
private val currencies = listOf(
    Currency.UAH,
    Currency.USD,
    Currency.EUR,
    Currency.RUB
)

private val currencyStrings = currencies.map { it.toString() }.toTypedArray()

class CurrencyPicker(context: Context, attrs: AttributeSet? = null) :
    NumberPicker(context, attrs) {

    init {
        minValue = 0
        maxValue = currencies.size - 1
        displayedValues = currencyStrings
    }

    var currency: Currency
        set(value) {
            // TODO. Ugly.
            this.value = currencies.indexOf(value)
        }
        get() {
            return currencies[this.value]
        }
}