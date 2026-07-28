package com.jagl.domain.model

import java.util.Locale

/**
 * Data model representing an exchange rate between two currencies
 */

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double
)

fun ExchangeRate.getEquivalent(locale: Locale): String {
    return "1 ${this.fromCurrency} = ${String.format(locale, "%.4f", this.rate)} ${this.toCurrency}"
}