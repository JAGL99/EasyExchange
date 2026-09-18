package com.jagl.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object GetCurrencies {

    @Serializable
    data class CurrencyDto(
        @SerialName("iso_code")
        val isoCode: String,

        @SerialName("iso_numeric")
        val isoNumeric: String? = null,

        @SerialName("name")
        val name: String,

        @SerialName("symbol")
        val symbol: String? = null,

        @SerialName("start_date")
        val startDate: String,

        @SerialName("end_date")
        val endDate: String
    )
}
