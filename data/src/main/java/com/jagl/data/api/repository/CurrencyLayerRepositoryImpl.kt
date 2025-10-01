package com.jagl.data.api.repository

import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.model.GetLatestRates

class CurrencyLayerRepositoryImpl(
    private val api: CurrencyLayerApi
) : ICurrencyLayerRepository {

    override suspend fun getLatestRates(request: GetLatestRates.Request): Result<GetLatestRates.Response> {
        try {
            val result = api.getLatestRates(
                source = request.source,
                currencies = request.currencies,
                accessKey = request.accessKey,
                format = request.format
            )

            if (!result.isSuccessful || result.body() == null) {
                return Result.failure(Exception(result.message()))
            }

            val mappedResult = result.body()!!
                .copy(
                quotes = result.body()!!.quotes?.mapKeys {
                    it.key.removePrefix(request.source)
                }
                )
            return Result.success(mappedResult)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}