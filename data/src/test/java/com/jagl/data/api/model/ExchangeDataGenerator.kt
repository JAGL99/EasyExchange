package com.jagl.data.api.model

import com.jagl.data.local.entity.ExchangeRateEntity
import java.time.Instant

fun exchangeRate(): ExchangeRateEntity {
    return ExchangeRateEntity(
        id = 1,
        timestamp = Instant.now().epochSecond,
        fromCurrency = "USD",
        toCurrency = "EUR",
        rate = 0.85,
        date = "2023-10-01"
    )
}

fun exchangeRates(): List<ExchangeRateEntity> {
    val arrayList = arrayListOf<ExchangeRateEntity>()
    repeat(15) {
        arrayList.add(
            exchangeRate().copy(
                id = it + 1,
                fromCurrency = "USD",
                toCurrency = "CUR$it",
                rate = 0.85 + it,
                date = "20$it-10-01"
            )
        )
    }
    return arrayList
}