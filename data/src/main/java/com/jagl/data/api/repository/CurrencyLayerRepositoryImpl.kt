package com.jagl.data.api.repository

import com.jagl.data.api.client.FrankfurterApi
import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.utils.ApiUtils.safeResultCall
import com.jagl.data.api.utils.ApiUtils.safeMap

/**
 * Implementation of the ICurrencyLayerRepository interface that uses the FrankfurterApi to fetch currency data.
 *
 * @param api The FrankfurterApi instance used to make API calls.
 */
class CurrencyLayerRepositoryImpl(
    private val api: FrankfurterApi
) : ICurrencyLayerRepository {

    /**
     * Fetches the latest exchange rates for a given base currency and a list of quote currencies.
     * @param request The request containing the base currency and quote currencies.
     * @return A Result containing a list of RateDto objects or an error.
     */
    override suspend fun getLatestRates(request: GetLatestRates.Request): Result<List<GetLatestRates.RateDto>> =
        safeResultCall {
            val response = api.getLatestRates(
                base = request.base,
                quotes = request.quotes
            )
            safeMap(response)
        }

    /**
     * Fetches the list of available currencies.
     * @return A Result containing a list of CurrencyDto objects or an error.
     */
    override suspend fun getCurrencies(): Result<List<GetCurrencies.CurrencyDto>> = safeResultCall {
        val response = api.getCurrencies()
        safeMap(response)
    }
}