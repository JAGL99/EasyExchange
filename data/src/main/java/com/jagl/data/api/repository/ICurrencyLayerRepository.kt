package com.jagl.data.api.repository

import com.jagl.data.api.model.CurrencyDto
import com.jagl.data.api.model.GetLatestRatesRequest
import com.jagl.data.api.model.RateDto

interface ICurrencyLayerRepository {

    /**
     * Access real-time exchange rates using the currencylayer API's live endpoint
     */
    suspend fun getLatestRates(
        request: GetLatestRatesRequest
    ): Result<List<RateDto>>

    suspend fun getCurrencies(): Result<List<CurrencyDto>>
}