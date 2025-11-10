package com.jagl.data.datasource.exchangeRate

import com.jagl.domain.model.Currency
import com.jagl.domain.model.ExchangeRate

fun interface IExchangeDataSource {

    suspend fun getExchangeRate(
        amount: Double,
        date: String,
        fromCurrency: Currency,
        toCurrency: Currency
    ): Result<ExchangeRate>
}