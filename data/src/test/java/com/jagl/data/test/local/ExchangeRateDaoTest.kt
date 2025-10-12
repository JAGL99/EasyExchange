package com.jagl.data.test.local

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.jagl.data.api.model.exchangeRate
import com.jagl.data.api.model.exchangeRates
import com.jagl.data.local.ExchangeRateDaoFake
import com.jagl.data.local.dao.ExchangeRateDao
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExchangeRateDaoTest {

    private lateinit var dao: ExchangeRateDao


    @BeforeEach
    fun setUp() {
        dao = ExchangeRateDaoFake()
    }

    @Test
    fun `Insert and retrieve exchange rate`() = runBlocking<Unit> {
        val singlerRate = exchangeRate()
        dao.insertExchangeRate(singlerRate)
        val result = dao.getExchangeRateForDate(
            fromCurrency = singlerRate.fromCurrency,
            toCurrency = singlerRate.toCurrency,
            date = singlerRate.date
        )
        assertThat(result).hasSize(1)
        assertThat(result).containsExactly(singlerRate)
    }

    @Test
    fun `Insert multiple exchange rates and retrieve them`() = runBlocking<Unit> {
        val multipleRates = exchangeRates().map {
            it.copy(
                fromCurrency = "USD",
                toCurrency = "EUR",
                date = "2023-10-01"
            )
        }
        dao.insertExchangeRates(multipleRates)
        val resultMultiple = dao.getExchangeRateForDate(
            fromCurrency = multipleRates.first().fromCurrency,
            toCurrency = multipleRates.first().toCurrency,
            date = multipleRates.first().date
        )
        assertThat(resultMultiple).containsExactly(*multipleRates.toTypedArray())
        assertThat(resultMultiple.first().timestamp <= resultMultiple.last().timestamp).isTrue()
    }

    @Test
    fun `Get exchange rate for specific date`() = runBlocking<Unit> {
        val rate01 = exchangeRates().map {
            it.copy(
                id = (1..150).random(),
                date = "2023-10-01",
                toCurrency = "EUR",
                fromCurrency = "USD"
            )
        }
        val rate02 = exchangeRates().map {
            it.copy(
                id = (1..150).random(),
                date = "2023-10-02",
                toCurrency = "EUR",
                fromCurrency = "MXN"
            )
        }
        dao.insertExchangeRates(rate01 + rate02)
        val resultDate01 = dao.getExchangeRateForDate(
            fromCurrency = rate01.first().fromCurrency,
            toCurrency = rate01.first().toCurrency,
            date = rate01.first().date
        )
        assertThat(resultDate01).hasSize(rate01.size)
        assertThat(resultDate01).containsExactly(*rate01.toTypedArray())
        val resultDate02 = dao.getExchangeRateForDate(
            fromCurrency = rate02.first().fromCurrency,
            toCurrency = rate02.first().toCurrency,
            date = rate02.first().date
        )
        assertThat(resultDate02).hasSize(rate02.size)
        assertThat(resultDate02).containsExactly(*rate02.toTypedArray())
    }

    @Test
    fun `Delete existing exchange rate`() = runBlocking<Unit> {
        val rate01 = exchangeRate().copy(
            id = 1,
            date = "2023-10-01",
            toCurrency = "EUR",
            fromCurrency = "USD"
        )
        val rate02 = exchangeRate().copy(
            id = 2,
            date = "2023-10-02",
            toCurrency = "EUR",
            fromCurrency = "MXN"
        )
        dao.insertExchangeRates(listOf(rate01, rate02))
        dao.deleteExchangeRate(rate01)
        val resultDate02 = dao.getExchangeRateForDate(
            fromCurrency = rate02.fromCurrency,
            toCurrency = rate02.toCurrency,
            date = rate02.date
        )
        assertThat(resultDate02).hasSize(1)
        assertThat(resultDate02.first()).isEqualTo(rate02)
    }

}