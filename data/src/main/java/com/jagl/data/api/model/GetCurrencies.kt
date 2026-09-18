package com.jagl.data.api.model

object GetCurrencies {

    data class Response(
        val currencies: List<Currencie>?,
        override val status: Int?,
        override val message: String?
    ) : CurrencyLayerResponseError

    data class Currencie(
        val isoCode: String,
        val isoNumeric: String,
        val name: String,
        val symbol: String,
        val startDate: String,
        val endDate: String
    )
}