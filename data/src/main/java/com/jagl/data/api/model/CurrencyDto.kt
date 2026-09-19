package com.jagl.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

object GetCurrencies {

    @JsonClass(generateAdapter = true)
    data class CurrencyDto(
        @Json(name = "iso_code")
        val isoCode: String? = null,

        @Json(name = "iso_numeric")
        val isoNumeric: String? = null,

        @Json(name = "name")
        val name: String? = null,

        @Json(name = "symbol")
        val symbol: String? = null,

        @Json(name = "start_date")
        val startDate: String? = null,

        @Json(name = "end_date")
        val endDate: String? = null
    )
}
