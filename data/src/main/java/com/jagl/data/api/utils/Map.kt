package com.jagl.data.api.utils

import com.jagl.data.api.model.GetCurrencies
import com.jagl.domain.model.Currency

fun  List<GetCurrencies.CurrencyDto>.toCurrencyList(): List<Currency> {
    return this.map {
        Currency(code = it.isoCode.orEmpty(), name = it.name.orEmpty())
    }
}