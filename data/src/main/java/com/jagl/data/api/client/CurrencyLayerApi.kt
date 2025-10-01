package com.jagl.data.api.client


import com.jagl.data.api.model.GetLatestRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para la API de tasas de cambio
 * Utilizaremos la API de apilayer.net
 * La API key ya está incluida en la URL base
 */
interface CurrencyLayerApi {

    /**
     * Obtiene las tasas de cambio para una moneda base
     * @param source Código de la moneda base (ej. USD, EUR)
     * @param currencies Lista de códigos de monedas separados por comas para obtener sus tasas
     * @return Respuesta con las tasas de cambio
     */
    @GET("live")
    suspend fun getLatestRates(
        @Query("source")
        source: String,
        @Query("currencies")
        currencies: String,
        @Query("access_key")
        accessKey: String,
        @Query("format")
        format: Int
    ): Response<GetLatestRates.Response>
}