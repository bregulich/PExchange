package com.bokugan.pexchange.entities

open class CurrencyPair(
    open val baseCurrency: Currency,
    open val quoteCurrency: Currency,
    open val buy: Double,
    open val sell: Double
) {
    fun baseToQuoteCurrency(amount: Double) = amount * buy
    fun quoteToBaseCurrency(amount: Double) = amount / sell

    fun createInverted() = CurrencyPair(
        quoteCurrency,
        baseCurrency,
        1 / sell,
        1 / buy
    )

    fun tryCreateCrossCurrency(other: CurrencyPair) =
        CurrencyPair.tryCreateCrossCurrency(this, other)

    companion object {

        fun tryCreateCrossCurrency(left: CurrencyPair, right: CurrencyPair): CurrencyPair? {
            val commonCcy = getCommonCurrency(left, right) ?: return null

            val newLeft = if (left.baseCurrency == commonCcy) left.createInverted() else left
            val newRight = if (right.baseCurrency == commonCcy) right.createInverted() else right

            return CurrencyPair(
                newLeft.baseCurrency,
                newRight.baseCurrency,
                newLeft.buy,
                newRight.sell
            )
        }

        private fun getCommonCurrency(left: CurrencyPair, right: CurrencyPair) =
            listOf(
                left.baseCurrency,
                left.quoteCurrency,
                right.baseCurrency,
                right.quoteCurrency
            )
                .groupBy { it }
                .filterValues { it.size == 2 }
                .values.firstOrNull()?.firstOrNull()

    }
}