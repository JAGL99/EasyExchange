package com.jagl.data.api.repository

import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates

interface ICurrencyLayerRepository {

    /**
     * Access real-time exchange rates using the currencylayer API's live endpoint
     */
    suspend fun getLatestRates(
       request: GetLatestRates.Request
    ): Result<List<GetLatestRates.RateDto>>

    suspend fun getCurrencies(): Result<List<GetCurrencies.CurrencyDto>>
}