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
 * Modelo de respuesta de la API de tasas de cambio de apilayer.net
 */
data class ExchangeRateResponse(
    val success: Boolean,
    val terms: String?,
    val privacy: String?,
    val timestamp: Long,
    val source: String,
    val quotes: Map<String, Double>
)