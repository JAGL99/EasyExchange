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
        @field:Json(name = "date")
        val date: String,
        @field:Json(name = "base")
        val base: String,
        @field:Json(name = "rates")
        val rate: Double,
        @field:Json(name = "quote")
        val quote: String
    )
}