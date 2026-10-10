package com.jagl.data.api.model

data class GetLatestRatesRequest(
    val base: String,
    val quotes: String,
)