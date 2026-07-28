package com.jagl.data.datasource.exchangeRate

import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency
import com.jagl.domain.model.ExchangeRate

/**
 * Interface for a data source that provides exchange rate information.
 */
fun interface IExchangeDataSource {

    /**
     * Gets the exchange rate for a given amount, date, and currencies.
     * @param amount The amount to convert.
     * @param date The date of the exchange rate.
     * @param fromCurrency The source currency.
     * @param toCurrency The target currency.
     * @return An [ApiState] with an [ExchangeRate] object.
     */
    suspend fun getExchangeRate(
        amount: Double,
        date: String,
        fromCurrency: Currency,
        toCurrency: Currency
    ): ApiState<ExchangeRate>
}