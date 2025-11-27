package com.jagl.data.api.model

object GetCurrencies {

    data class Response(
        val success: Boolean,
        val terms: String?,
        val privacy: String?,
        val error: ApiError?,
        val currencies: Map<String, String>?
    )

}