package com.jagl.data.api.repository

import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.utils.ApiUtils.safeResultCall
import com.jagl.data.api.utils.ApiUtils.safeMap

class CurrencyLayerRepositoryImpl(
    private val api: CurrencyLayerApi
) : ICurrencyLayerRepository {

    override suspend fun getLatestRates(request: GetLatestRates.Request): Result<GetLatestRates.Response> =
        safeResultCall {
            val response = api.getLatestRates(
                source = request.source,
                currencies = request.currencies,
                format = request.format
            )

            safeMap(response) { body ->
                body.copy(
                    quotes = body.quotes?.mapKeys {
                        it.key.removePrefix(request.source)
                    }
                )
            }
        }

    override suspend fun getCurrencies(): Result<GetCurrencies.Response> = safeResultCall {
        val response = api.getCurrencies()
        safeMap(response)
    }
}