package com.bokugan.pexchange.entities

// https://www.iban.com/currency-codes
enum class Currency(val code: Int) {
    UAH(980),
    USD(840),
    EUR(978),
    RUB(643);

    companion object {
        private val types = values().associateBy { it.code }
        operator fun get(code: Int) =
            types[code] ?: throw Error("Invalid currency code")
    }
}