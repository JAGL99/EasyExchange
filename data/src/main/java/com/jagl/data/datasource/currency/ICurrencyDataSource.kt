package com.jagl.data.datasource.currency

import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency

/**
 * Interface for a data source that provides currency information.
 */
fun interface ICurrencyDataSource {
    /**
     * Gets the list of available currencies.
     * @return An [ApiState] with a list of [Currency] objects.
     */
    suspend fun getAvailableCurrencies(): ApiState<List<Currency>>
}