package com.jagl.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

object GetLatestRates {

    data class Request(
        val base: String,
        val quotes: String,
    )

    @JsonClass(generateAdapter = true)
    data class RateDto(
        @Json(name = "date")
        val date: String,
        @Json(name = "base")
        val base: String,
        @Json(name = "rate")
        val rate: Double,
        @Json(name = "quote")
        val quote: String
    )
}