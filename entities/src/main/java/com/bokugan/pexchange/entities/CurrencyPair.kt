package com.bokugan.pexchange.entities

open class CurrencyPair(
    open val baseCurrency: Currency,
    open val quoteCurrency: Currency,
    open val buy: Double,
    open val sell: Double
) {
    fun baseToQuoteCurrency(amount: Double) = amount / sell
    fun quoteToBaseCurrency(amount: Double) = amount * buy

    fun createInverted() = CurrencyPair(
        quoteCurrency,
        baseCurrency,
        1 / sell,
        1 / buy
    )

    fun tryCreateFromCrossCurrency(other: CurrencyPair) =
        CurrencyPair.tryCreateFromCrossCurrency(this, other)

    companion object {

        fun tryCreateFromCrossCurrency(left: CurrencyPair, right: CurrencyPair): CurrencyPair? {
            val commonCurrencies = getCommonCurrencies(left, right)
            if (commonCurrencies.size != 1) {
                return null
            }

            val commonCcy = commonCurrencies.first()
            val newLeft = if (left.baseCurrency == commonCcy) left.createInverted() else left
            val newRight = if (right.baseCurrency == commonCcy) right.createInverted() else right

            return CurrencyPair(
                newLeft.baseCurrency,
                newRight.quoteCurrency,
                newLeft.buy,
                newRight.sell
            )
        }

        private fun getCommonCurrencies(left: CurrencyPair, right: CurrencyPair) =
            setOf(
                left.baseCurrency,
                left.quoteCurrency,
                right.baseCurrency,
                right.quoteCurrency
            )
    }
}