package com.jagl.data.api.model

object GetCurrencies {

    data class Response(
        override val success: Boolean,
        override val terms: String?,
        override val privacy: String?,
        override val error: ApiError?,
        val currencies: Map<String, String>?
    ) : CurrencyLayerResponse

}