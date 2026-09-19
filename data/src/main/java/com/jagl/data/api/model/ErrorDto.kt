package com.jagl.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
data class ErrorDto(
    @Json(name = "status")
    val status: Int? = null,
    @Json(name = "message")
    val message: String? = null
)