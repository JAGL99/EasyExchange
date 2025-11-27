package com.jagl.data.api.model

object GetLatestRates {

    data class Request(
        val source: String,
        val currencies: String,
        val format: Int = 1
    )

    data class Response(
        val success: Boolean,
        val terms: String?,
        val privacy: String?,
        val error: ApiError?,
        val timestamp: Long?,
        val source: String?,
        val quotes: Map<String, Double>?
    )
}