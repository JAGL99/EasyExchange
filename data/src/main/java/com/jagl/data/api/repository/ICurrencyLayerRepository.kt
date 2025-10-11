package com.jagl.data.api.repository

import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates

interface ICurrencyLayerRepository {

    /**
     * Access real-time exchange rates using the currencylayer API's live endpoint
     */
    suspend fun getLatestRates(
       request: GetLatestRates.Request
    ): Result<GetLatestRates.Response>

    suspend fun getCurrencies(
        request: GetCurrencies.Request
    ): Result<GetCurrencies.Response>
}