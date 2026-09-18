package com.jagl.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object GetLatestRates {

    data class Request(
        val base: String,
        val quotes: String,
    )

    @Serializable
    data class RateDto(
        @SerialName("date")
        val date: String,
        @SerialName("base")
        val base: String,
        @SerialName("rate")
        val rate: Double,
        @SerialName("quote")
        val quote: String
    )
}