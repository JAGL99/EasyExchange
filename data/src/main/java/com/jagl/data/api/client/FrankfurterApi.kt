package com.jagl.data.api.client


import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the frankfurter API.
 */
interface FrankfurterApi {

    /**
     * Gets the exchange rates for a base currency
     * @param base Base currency code (e.g., USD, EUR)
     * @param quotes Comma-separated list of currency codes to get their rates
     * @return Response with the exchange rates
     */
    @GET("rates")
    suspend fun getLatestRates(
        @Query("base")
        base: String,
        @Query("quotes")
        quotes: String
    ): Response<GetLatestRates.Response>

    /**
     * Gets the list of available currencies
     * @return Response with the list of currencies
     */

    @GET("currencies")
    suspend fun getCurrencies(): Response<GetCurrencies.Response>
}