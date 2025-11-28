package com.jagl.data.api.model

object GetLatestRates {

    data class Request(
        val source: String,
        val currencies: String,
        val format: Int = 1
    )

    data class Response(
        override val success: Boolean,
        override val terms: String?,
        override val privacy: String?,
        override val error: ApiError?,
        val timestamp: Long?,
        val source: String?,
        val quotes: Map<String, Double>?
    ) : CurrencyLayerResponse
}