package com.jagl.data.local

import com.jagl.data.local.dao.CurrencyDao
import com.jagl.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class CurrencyDaoFake : CurrencyDao {

    private val currencies = MutableStateFlow<List<CurrencyEntity>>(emptyList())

    override suspend fun insertCurrency(exchangeRate: CurrencyEntity) {
        val currentValue = currencies.value
        if (currentValue.any { it.code == exchangeRate.code }) {
            return
        } else {
            currencies.update { currentValue + exchangeRate }
        }
    }

    override suspend fun insertCurrencies(exchangeRates: List<CurrencyEntity>) {
        val currentValue = currencies.value
        val newRates = exchangeRates.filter { newRate ->
            currentValue.none { it.code == newRate.code }
        }
        currencies.update { currentValue + newRates }
    }

    override suspend fun getCurrencies(): List<CurrencyEntity> {
        return currencies.value
    }

    override suspend fun getCurrencyByCode(code: String): CurrencyEntity? {
        return currencies.value.firstOrNull { it.code == code }
    }
}