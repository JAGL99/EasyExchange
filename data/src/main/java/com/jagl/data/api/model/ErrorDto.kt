package com.jagl.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("status")
    val status: Int? = null,
    @SerialName("message")
    val message: String? = null
)