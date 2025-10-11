package com.jagl.data.api.utils

import com.jagl.domain.model.Currency

fun Map<String, String>.toCurrencyList(): List<Currency> {
    return this.map { (code, name) ->
        Currency(code = code, name = name)
    }
}