package com.jagl.data.api.repository

import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates
import retrofit2.Response

class CurrencyLayerRepositoryImpl(
    private val api: CurrencyLayerApi
) : ICurrencyLayerRepository {

    private fun <T> safeCall(response: Response<T>, onMapResponse: ((T) -> T)? = null): Result<T> {
        try {
            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(Exception(response.message()))
            }
            val body = response.body()!!

            return when (onMapResponse == null) {
                true -> Result.success(body)
                false -> Result.success(onMapResponse(body))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getLatestRates(request: GetLatestRates.Request): Result<GetLatestRates.Response> {
        if (request.accessKey.isEmpty()) {
            return Result.failure(Exception())
        }


        val response = api.getLatestRates(
            source = request.source,
            currencies = request.currencies,
            accessKey = request.accessKey,
            format = request.format
        )
        return safeCall(response) { body ->
            body.copy(
                quotes = body.quotes?.mapKeys {
                    it.key.removePrefix(request.source)
                }
            )
        }
    }

    override suspend fun getCurrencies(request: GetCurrencies.Request): Result<GetCurrencies.Response> {
        if (request.accessKey.isEmpty()) {
            return Result.failure(Exception())
        }
        val response = api.getCurrencies(accessKey = request.accessKey)
        return safeCall(response)
    }
}