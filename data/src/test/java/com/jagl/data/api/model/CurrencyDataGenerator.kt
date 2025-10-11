package com.jagl.data.api.model

import com.jagl.data.local.entity.CurrencyEntity


fun currencyEntity(): CurrencyEntity {
    return CurrencyEntity.fromCurrency(getCurrencies().first())
}

fun currenciesEntity(): List<CurrencyEntity> {
    return getCurrencies().map(CurrencyEntity::fromCurrency)
}
