package com.jagl.data.api.repository

import com.jagl.data.api.model.GetLatestRates

fun interface ICurrencyLayerRepository {

    /**
     * Access real-time exchange rates using the currencylayer API's live endpoint
     */
    suspend fun getLatestRates(
       request: GetLatestRates.Request
    ): Result<GetLatestRates.Response>
}