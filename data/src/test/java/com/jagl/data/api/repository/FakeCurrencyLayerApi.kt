package com.jagl.data.api.repository

import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.model.getCurrencies
import com.jagl.data.api.model.getCurrenciesResponse
import com.jagl.data.api.model.getLatestRatesResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response

class FakeCurrencyLayerApi : CurrencyLayerApi {

    override suspend fun getLatestRates(
        source: String,
        currencies: String,
        accessKey: String,
        format: Int
    ): Response<GetLatestRates.Response> {
        val avableCurrencies = getCurrencies()
        val response = getLatestRatesResponse(
            source = source,
            currencies = currencies,
            avableCurrencies = avableCurrencies
        )
        return when(response.success){
            true ->  Response.success(response)
            false -> {
                val errorBody = ResponseBody.create("application/json".toMediaTypeOrNull(), response.toString())
                Response.error(400, errorBody)
            }
        }
    }

    override suspend fun getCurrencies(accessKey: String): Response<GetCurrencies.Response> {
        accessKey.ifEmpty {
            val errorBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "")
            return Response.error(400, errorBody)
        }
        val response = getCurrenciesResponse()
        return Response.success(response)
    }


}