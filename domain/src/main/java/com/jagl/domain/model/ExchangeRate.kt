package com.jagl.domain.model

import java.util.Locale

/**
 * Data model representing an exchange rate between two currencies
 */

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val baseRate: Double
){
    companion object{
        val  previewObject = ExchangeRate("USD", "MXN", 24.0,12.0)
    }

}

fun ExchangeRate.getEquivalent(locale: Locale): String {
    return "1 ${this.fromCurrency} = ${String.format(locale, "%.4f", this.baseRate)} ${this.toCurrency}"
}