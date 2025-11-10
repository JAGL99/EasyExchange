package com.jagl.domain.model

import java.util.Locale

/**
 * Modelo de datos que representa una tasa de cambio entre dos monedas
 */

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double
)

fun ExchangeRate.getEquivalent(locale: Locale): String {
    return "1 ${this.fromCurrency} = ${String.format(locale, "%.4f", this.rate)} ${this.toCurrency}"
}