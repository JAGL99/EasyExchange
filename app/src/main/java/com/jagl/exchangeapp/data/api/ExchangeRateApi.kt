package com.jagl.exchangeapp.data.api

import com.jagl.exchangeapp.data.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para la API de tasas de cambio
 * Utilizaremos la API de apilayer.net
 * La API key ya está incluida en la URL base
 */
interface ExchangeRateApi {

    /**
     * Obtiene las tasas de cambio para una moneda base
     * @param source Código de la moneda base (ej. USD, EUR)
     * @param currencies Lista de códigos de monedas separados por comas para obtener sus tasas
     * @return Respuesta con las tasas de cambio
     */
    @GET("live")
    suspend fun getLatestRates(
        @Query("source") source: String,
        @Query("currencies") currencies: String,
        @Query("access_key") accessKey: String = "83b42c4384534c5fed4f6e9685c09953",
        @Query("format") format: Int = 1
    ): ExchangeRateResponse
}