package com.jagl.data.datasource.exchangeRate

fun interface IExchangeDataSource {

    suspend fun convertCurrency(
        amount: Double,
        date: String,
        fromCurrency: String,
        toCurrency: String
    ): Result<Double>
}