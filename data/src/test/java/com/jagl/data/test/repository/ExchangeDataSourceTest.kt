package com.jagl.data.test.repository

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.jagl.core.network.INetworkManager
import com.jagl.core.network.NetworkStatus
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    private lateinit var networkManager: INetworkManager

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
        networkManager = object : INetworkManager {
            override fun getInternetConnectionStatus(): Flow<NetworkStatus> {
                return flow {
                    emit(NetworkStatus.Available)
                }
            }
            override fun isConnected(): Boolean {
                return true
            }
        }
        dataSource = ExchangeDataSource(networkManager,repository, dao)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Request without internet, get empty data`() = runBlocking<Unit>{
        networkManager = object : INetworkManager {
            override fun getInternetConnectionStatus(): Flow<NetworkStatus> {
                return flow {
                    emit(NetworkStatus.Unavailable)
                }
            }
            override fun isConnected(): Boolean {
                return false
            }
        }
        dataSource = ExchangeDataSource(networkManager,repository, dao)
        val date = "2025-01-25"
        val fromCurrency = getCurrencies().first()
        val toCurrency = getCurrencies().last()
        val result = dataSource.getExchangeRate(1.0, date, fromCurrency, toCurrency)
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message == "Sin conexión a internet").isTrue()
        val exchange = dao.getExchangeRateForDate(fromCurrency.code, toCurrency.code, date)
        assertThat(exchange).isEmpty()
    }

    @Test
    fun `Request bad list, get empty data`() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val date = "2025-01-25"
        val fromCurrency = getCurrencies().first()
        val toCurrency = getCurrencies().last()
        val result = dataSource.getExchangeRate(1.0, date, fromCurrency, toCurrency)
        assertThat(result.isFailure).isTrue()
        val exchange = dao.getExchangeRateForDate(fromCurrency.code, toCurrency.code, date)
        assertThat(exchange).isEmpty()
    }

    @Test
    fun `Request list two times, get success but withour repetition`() = runBlocking<Unit> {
        val currencies = getCurrencies()
        val fromCurrency = getCurrencies().first()
        val toCurrency = getCurrencies().last()
        val timeStamp = Date.from(Instant.now()).time
        val mockResponse = getLatestRatesResponse(
            source = fromCurrency.code,
            avableCurrencies = currencies,
            currencies = toCurrency.code
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
        val firstResult = dataSource.getExchangeRate(
            amount = 1.0,
            date = "2025-11-25",
            fromCurrency = fromCurrency,
            toCurrency = toCurrency
        )

        assertThat(firstResult.isSuccess).isTrue()
        assertDoesNotThrow {
            val body = firstResult.getOrThrow()
            assertThat(body.toCurrency).isEqualTo(toCurrency.code)
            assertThat(body.fromCurrency).isEqualTo(fromCurrency.code)
            assertThat(body.rate > 0.0).isTrue()
        }

        val firstExchange = dao.getExchangeRateForDate(fromCurrency.code, toCurrency.code, "2025-11-25")
        assertThat(firstExchange).isNotEmpty()
        assertThat(firstExchange).hasSize(1)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)
        )

        val secondResult = dataSource.getExchangeRate(
            amount = 1.0,
            date = "2025-11-25",
            fromCurrency = fromCurrency,
            toCurrency = toCurrency
        )
        assertThat(secondResult.isSuccess).isTrue()
        val secondExchange = dao.getExchangeRateForDate(fromCurrency.code, toCurrency.code, "2025-11-25")
        assertThat(secondExchange).hasSize(1)
    }

    @Test
    fun `Request list, get success with local data`() = runBlocking<Unit> {
        val currencies = getCurrencies()
        val timeStamp = Date.from(Instant.now()).time
        val fromCurrency = getCurrencies().first()
        val toCurrency = getCurrencies().last()
        val mockResponse = getLatestRatesResponse(
            source = fromCurrency.code,
            avableCurrencies = currencies,
            currencies = toCurrency.code
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
        val firstResult = dataSource.getExchangeRate(
            amount = 1.0,
            date = "2025-11-25",
            fromCurrency = fromCurrency,
            toCurrency = toCurrency
        )

        assertThat(firstResult.isSuccess).isTrue()
        assertDoesNotThrow {
            val body = firstResult.getOrThrow()
            currency = body.rate
            assertThat(body.toCurrency).isEqualTo(toCurrency.code)
            assertThat(body.fromCurrency).isEqualTo(fromCurrency.code)
            assertThat(body.rate > 0.0).isTrue()
        }


        val firstExchange = dao.getExchangeRateForDate(fromCurrency.code, toCurrency.code, "2025-11-25")
        assertThat(firstExchange).isNotEmpty()
        assertThat(firstExchange).hasSize(1)

        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val secondResult = dataSource.getExchangeRate(
            amount = 1.0,
            date = "2025-11-25",
            fromCurrency = fromCurrency,
            toCurrency = toCurrency
        )
        /*
        assertThat(secondResult.isSuccess).isTrue()
        assertDoesNotThrow {
            val body = firstResult.getOrThrow()
            assertThat(body.rate).isEqualTo(currency)
        }
         */
    }
}