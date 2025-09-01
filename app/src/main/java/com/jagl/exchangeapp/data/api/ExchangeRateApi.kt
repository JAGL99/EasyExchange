package com.jagl.exchangeapp.data.api

import com.jagl.exchangeapp.data.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para la API de tasas de cambio
 * Utilizaremos la API gratuita de ExchangeRate-API (https://www.exchangerate-api.com/)
 * Nota: En una aplicación real, deberías registrarte para obtener una clave API
 */
interface ExchangeRateApi {
    
    /**
     * Obtiene las tasas de cambio para una moneda base
     * @param base Código de la moneda base (ej. USD, EUR)
     * @return Respuesta con las tasas de cambio
     */
    @GET("latest")
    suspend fun getLatestRates(@Query("base") base: String): ExchangeRateResponse
}