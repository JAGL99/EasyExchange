package com.jagl.data.test.local

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.jagl.data.api.model.currenciesEntity
import com.jagl.data.api.model.currencyEntity
import com.jagl.data.local.CurrencyDaoFake
import com.jagl.data.local.dao.CurrencyDao
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CurrencyDaoTest {

    private lateinit var dao: CurrencyDao

    @BeforeEach
    fun setUp() {
        dao = CurrencyDaoFake()
    }


    @Test
    fun `Insert new currency and re-insert currency, get only one currency`()= runBlocking<Unit>{
        val currency = currencyEntity()
        dao.insertCurrency(currency)
        dao.insertCurrency(currency)
        val result = dao.getCurrencies()
        assertThat(result).containsExactly(currency)
    }

    @Test
    fun `Insert multiple currencies and re-insert currencies, get the same list`()= runBlocking<Unit>{
        val currencies = currenciesEntity()
        dao.insertCurrencies(currencies)
        dao.insertCurrencies(currencies)
        val result = dao.getCurrencies()
        assertThat(result).containsExactly(*currencies.toTypedArray())
    }

    @Test
    fun `Request currencies without data, get empty list`() = runBlocking<Unit>{
        val curencies = dao.getCurrencies()
        assertThat(curencies).isEmpty()
    }

    @Test
    fun `Request currency by code, get currency if exist`()= runBlocking<Unit>{
        val firstCurrency = dao.getCurrencyByCode("")
        assertThat(firstCurrency).isNull()
        val currency = currencyEntity()
        dao.insertCurrency(currency)
        val secondCurrency = dao.getCurrencyByCode(currency.code)
        assertThat(secondCurrency).isNotNull()
        assertThat(secondCurrency).isEqualTo(currency)
    }

}