package com.jagl.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

object GetCurrencies {

    @JsonClass(generateAdapter = true)
    data class CurrencyDto(
        @field:Json(name = "iso_code")
        val iso_code: String? = null,

        @field:Json(name = "iso_numeric")
        val iso_numeric: String? = null,

        @field:Json(name = "name")
        val name: String? = null,

        @field:Json(name = "symbol")
        val symbol: String? = null,

        @field:Json(name = "start_date")
        val start_date: String? = null,

        @field:Json(name = "end_date")
        val end_date: String? = null
    )
}
