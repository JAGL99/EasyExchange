package com.jagl.data.api.client


import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the exchange rates API.
 * We will use the apilayer.net API.
 * The API key is already included in the base URL.
 */
interface CurrencyLayerApi {

    /**
     * Gets the exchange rates for a base currency
     * @param source Base currency code (e.g., USD, EUR)
     * @param currencies Comma-separated list of currency codes to get their rates
     * @return Response with the exchange rates
     */
    @GET("live")
    suspend fun getLatestRates(
        @Query("source")
        source: String,
        @Query("currencies")
        currencies: String,
        @Query("format")
        format: Int
    ): Response<GetLatestRates.Response>

    @GET("list")
    suspend fun getCurrencies(): Response<GetCurrencies.Response>
}