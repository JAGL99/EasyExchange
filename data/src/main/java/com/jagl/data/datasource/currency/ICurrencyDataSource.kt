package com.jagl.data.datasource.currency

import com.jagl.domain.model.Currency

fun interface ICurrencyDataSource {
    suspend fun getAvailableCurrencies(): List<Currency>
}