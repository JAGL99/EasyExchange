package com.jagl.data.local.database

import com.jagl.data.local.dao.CurrencyDao
import com.jagl.data.local.dao.ExchangeRateDao

interface IExchangeDatabase {
    fun exchangeRateDao(): ExchangeRateDao
    fun currencyDao(): CurrencyDao
}
