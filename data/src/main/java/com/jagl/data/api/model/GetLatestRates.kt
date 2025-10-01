package com.jagl.data.api.model

object GetLatestRates {

    data class Request(
        val source: String,
        val currencies: String,
        val accessKey: String = "83b42c4384534c5fed4f6e9685c09953",
        val format: Int = 1
    )

    data class Response(
        val success: Boolean,
        val terms: String?,
        val privacy: String?,
        val error: Error?,
        val timestamp: Long?,
        val source: String?,
        val quotes: Map<String, Double>?
    )

    data class Error(
        val code: Int,
        val info: String
    )
}