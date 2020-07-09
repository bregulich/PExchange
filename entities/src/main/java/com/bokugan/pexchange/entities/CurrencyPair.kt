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

    // IMPORTANT. May lose some precision during conversion or i did something wrong x_x
    fun tryCreateCrossCurrency(other: CurrencyPair) =
        CurrencyPair.tryCreateCrossCurrency(this, other)

    companion object {

        // IMPORTANT. May lose some precision during conversion or i did something wrong x_x
        fun tryCreateCrossCurrency(left: CurrencyPair, right: CurrencyPair): CurrencyPair? {
            val commonCcy = getCommonCurrency(left, right) ?: return null

            val newLeft = if (left.baseCurrency == commonCcy) left.createInverted() else left
            val newRight = if (right.quoteCurrency == commonCcy) right.createInverted() else right

            return CurrencyPair(
                newLeft.baseCurrency,
                newRight.quoteCurrency,
                newLeft.buy * newRight.buy,
                newLeft.sell * newRight.sell
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