package com.jagl.domain.model

import java.util.Date

/**
 * Modelo de datos que representa una tasa de cambio entre dos monedas
 */

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val timestamp: Date = Date()
)