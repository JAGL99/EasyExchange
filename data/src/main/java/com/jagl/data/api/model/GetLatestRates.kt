package com.jagl.data.api.model

object GetLatestRates {

    data class Request(
        val base: String,
        val quotes: String,
    )

    data class Response(
        val rate : Rate?,
        override val status: Int?,
        override val message: String?
    ) : CurrencyLayerResponseError

    data class Rate(
        val date : String,
        val base : String,
        val range: Double,
        val quote: String
    )
}