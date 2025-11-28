package com.jagl.data.api.model

interface CurrencyLayerResponse {
    val success: Boolean
    val terms: String?
    val privacy: String?
    val error: ApiError?
}