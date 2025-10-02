package com.jagl.data.api.model

import com.jagl.domain.model.Currency

object GetCurrencies {

    data class Request(
        val accessKey: String = "83b42c4384534c5fed4f6e9685c09953"
    )

    data class Response(
        val success: Boolean,
        val terms: String?,
        val privacy: String?,
        val error: Error?,
        val currencies: List<Currency>?
    )

    data class Error(
        val code: Int,
        val info: String
    )
}