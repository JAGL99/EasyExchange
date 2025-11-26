package com.jagl.data.datasource.currency

import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency

fun interface ICurrencyDataSource {
    suspend fun getAvailableCurrencies(): ApiState<List<Currency>>
}