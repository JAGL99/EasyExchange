package com.jagl.data.api.model

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