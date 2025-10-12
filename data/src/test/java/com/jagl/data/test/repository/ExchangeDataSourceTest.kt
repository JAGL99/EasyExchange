package com.jagl.data.test.repository

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.model.getCurrencies
import com.jagl.data.api.model.getLatestRatesResponse
import com.jagl.data.api.repository.CurrencyLayerRepositoryImpl
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.datasource.exchangeRate.ExchangeDataSource
import com.jagl.data.datasource.exchangeRate.IExchangeDataSource
import com.jagl.data.local.ExchangeRateDaoFake
import com.jagl.data.local.dao.ExchangeRateDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.time.Instant
import java.util.Date

class ExchangeDataSourceTest {

    private lateinit var dao: ExchangeRateDao
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi
    private lateinit var repository: ICurrencyLayerRepository
    private lateinit var dataSource: IExchangeDataSource

    @BeforeEach
    fun setUp() {
        dao = ExchangeRateDaoFake()
        mockWebServer = MockWebServer()
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val api: CurrencyLayerApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
        repository = CurrencyLayerRepositoryImpl(api)
        dataSource = ExchangeDataSource(repository, dao)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Request bad list, get empty data`() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val date = "2025-01-25"
        val result = dataSource.convertCurrency(1.0, date, "USD", "MXN")
        assertThat(result.isFailure).isTrue()
        val exchange = dao.getExchangeRateForDate("USD", "MXN", date)
        assertThat(exchange).isEmpty()
    }

    @Test
    fun `Request list two times, get success but withour repetition`() = runBlocking<Unit> {
        val currencies = getCurrencies()
        val timeStamp = Date.from(Instant.now()).time
        val mockResponse = getLatestRatesResponse(
            source = "USD",
            avableCurrencies = currencies,
            currencies = "MXN"
        )
            .copy(
                timestamp = timeStamp
            )
        val adapter = moshi.adapter(GetLatestRates.Response::class.java)
        val mockResponseJson = adapter.toJson(mockResponse)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)
        )
        val firstResult = dataSource.convertCurrency(
            amount = 1.0,
            date = "2025-11-25",
            fromCurrency = "USD",
            toCurrency = "MXN"
        )
        assertThat(firstResult.isSuccess).isTrue()
        assertDoesNotThrow {
            val body = firstResult.getOrThrow()
            assertThat(body > 0.0).isTrue()
        }

        val firstExchange = dao.getExchangeRateForDate("USD", "MXN", "2025-11-25")
        assertThat(firstExchange).isNotEmpty()
        assertThat(firstExchange).hasSize(1)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)
        )

        val secondResult = dataSource.convertCurrency(
            amount = 1.0,
            date = "2025-11-25",
            fromCurrency = "USD",
            toCurrency = "MXN"
        )
        assertThat(secondResult.isSuccess).isTrue()
        val secondExchange = dao.getExchangeRateForDate("USD", "MXN", "2025-11-25")
        assertThat(secondExchange).hasSize(1)
    }

    @Test
    fun `Request list, get success with local data`() = runBlocking<Unit> {
        val currencies = getCurrencies()
        val timeStamp = Date.from(Instant.now()).time
        val mockResponse = getLatestRatesResponse(
            source = "USD",
            avableCurrencies = currencies,
            currencies = "MXN"
        )
            .copy(
                timestamp = timeStamp
            )
        val adapter = moshi.adapter(GetLatestRates.Response::class.java)
        val mockResponseJson = adapter.toJson(mockResponse)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)
        )
        var currency: Double = 0.0
        val firstResult = dataSource.convertCurrency(
            amount = 1.0,
            date = "2025-11-25",
            fromCurrency = "USD",
            toCurrency = "MXN"
        )
        assertThat(firstResult.isSuccess).isTrue()
        assertDoesNotThrow {
            val body = firstResult.getOrThrow()
            currency = body
            assertThat(body > 0.0).isTrue()
        }

        val firstExchange = dao.getExchangeRateForDate("USD", "MXN", "2025-11-25")
        assertThat(firstExchange).isNotEmpty()
        assertThat(firstExchange).hasSize(1)

        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val secondResult = dataSource.convertCurrency(
            amount = 1.0,
            date = "2025-11-25",
            fromCurrency = "USD",
            toCurrency = "MXN"
        )
        assertThat(secondResult.isSuccess).isTrue()
        assertDoesNotThrow {
            val body = firstResult.getOrThrow()
            assertThat(body).isEqualTo(currency)
        }
    }
}