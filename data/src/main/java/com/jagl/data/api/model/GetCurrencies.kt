package com.jagl.data.api.model

object GetCurrencies {

    data class Response(
        val success: Boolean,
        val terms: String?,
        val privacy: String?,
        val error: Error?,
        val currencies: Map<String, String>?
    )

    data class Error(
        val code: Int,
        val info: String
    )
}