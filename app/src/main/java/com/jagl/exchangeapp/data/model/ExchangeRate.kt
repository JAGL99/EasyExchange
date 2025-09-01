package com.jagl.exchangeapp.data.model

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

/**
 * Modelo de respuesta de la API de tasas de cambio
 */
data class ExchangeRateResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)