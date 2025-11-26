package com.jagl.data.api.model


data class CurrencyLayerError(
    val success: Boolean,
    val error: ApiError
)

data class ApiError(
    val code: Int,
    val type: String,
    val info: String
)
