package com.jagl.data.local

import com.jagl.data.local.dao.ExchangeRateDao
import com.jagl.data.local.entity.ExchangeRateEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate


class ExchangeRateDaoFake : ExchangeRateDao {

    private val exchangeRates = MutableStateFlow<List<ExchangeRateEntity>>(emptyList())

    override suspend fun insertExchangeRate(exchangeRate: ExchangeRateEntity) {
        exchangeRates.getAndUpdate { it + exchangeRate }
    }

    override suspend fun insertExchangeRates(exchangeRates: List<ExchangeRateEntity>) {
        this.exchangeRates.getAndUpdate { it + exchangeRates }
    }

    override suspend fun getExchangeRateForDate(
        fromCurrency: String,
        toCurrency: String,
        date: String
    ): List<ExchangeRateEntity> {
        val exchanges = exchangeRates.value.filter {
            it.date == date &&
                    it.toCurrency == toCurrency &&
                    it.fromCurrency == fromCurrency
        }.sortedBy { it.date }.sortedBy { it.timestamp }
        return exchanges
    }

    override suspend fun deleteExchangeRate(exchangeRate: ExchangeRateEntity) {
        exchangeRates.getAndUpdate {
            it.filter { it.id != exchangeRate.id }
        }
    }
}