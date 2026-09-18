package com.jagl.data.api.repository

import com.jagl.data.api.client.FrankfurterApi
import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.utils.ApiUtils.safeResultCall
import com.jagl.data.api.utils.ApiUtils.safeMap

class CurrencyLayerRepositoryImpl(
    private val api: FrankfurterApi
) : ICurrencyLayerRepository {

    override suspend fun getLatestRates(request: GetLatestRates.Request): Result<GetLatestRates.Response> =
        safeResultCall {
            val response = api.getLatestRates(
                base = request.base,
                quotes = request.quotes,
                format = request.format
            )

            safeMap(response) { body ->
                body.copy(
                    quotes = body.quotes?.mapKeys {
                        it.key.removePrefix(request.base)
                    }
                )
            }
        }

    override suspend fun getCurrencies(): Result<GetCurrencies.Response> = safeResultCall {
        val response = api.getCurrencies()
        safeMap(response)
    }
}