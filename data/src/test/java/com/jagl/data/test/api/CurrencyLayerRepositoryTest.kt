package com.jagl.data.test.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.model.getCurrencies
import com.jagl.data.api.model.getCurrenciesResponse
import com.jagl.data.api.model.getLatestRatesRequest
import com.jagl.data.api.model.getLatestRatesResponse
import com.jagl.data.api.repository.CurrencyLayerRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class CurrencyLayerRepositoryTest {

    private lateinit var repository: CurrencyLayerRepositoryImpl
    private lateinit var api: CurrencyLayerApi
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
        repository = CurrencyLayerRepositoryImpl(api)
    }


    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/http_codes.csv"])
    fun `Request currencies with 400 and 500 errors, get failure response with message`(code: String) =
        runBlocking<Unit> {
            mockWebServer.enqueue(MockResponse().setResponseCode(code.toInt()))
            val currencyResult = repository.getCurrencies()
            assertThat(currencyResult.isFailure).isTrue()
            val currencyMessage = currencyResult.exceptionOrNull()?.message
            assertThat(currencyMessage).isNotNull()
            assertThat(currencyMessage!!).isNotEmpty()
        }

    @ParameterizedTest
    @CsvFileSource(resources = ["/http_codes.csv"])
    fun `Request rates with 400 and 500 errors, get failure response with message`(code: String) =
        runBlocking<Unit> {
            mockWebServer.enqueue(MockResponse().setResponseCode(code.toInt()))
            val ratesResult = repository.getLatestRates(getLatestRatesRequest())
            assertThat(ratesResult.isFailure).isTrue()
            val ratesMessage = ratesResult.exceptionOrNull()?.message
            assertThat(ratesMessage).isNotNull()
            assertThat(ratesMessage!!).isNotEmpty()
        }


    @Test
    fun `Make a request for currencies, get the same data`() = runBlocking<Unit> {
        val adapter = moshi.adapter(GetCurrencies.Response::class.java)
        val mockResponse = getCurrenciesResponse()
        val mockResponseJson = adapter.toJson(mockResponse)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)

        )
        val result = repository.getCurrencies()
        assertThat(result).isInstanceOf(Result::class)
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        val currencies = result.getOrNull()!!.currencies
        assertThat(currencies).isNotNull()
        mockResponse.currencies!!.forEach { key, value ->
            val name = currencies?.get(key)
            assertThat(name).isNotNull()
            assertThat(name!!).isNotEmpty()
            assertThat(name).isEqualTo(value)
        }
    }

    @Test
    fun `Make a request for rates, get the same data`() = runBlocking {
        val avableCurrencies = getCurrencies()
        val source = avableCurrencies.first().code
        val currencies = avableCurrencies.last().code
        val adapter = moshi.adapter(GetLatestRates.Response::class.java)
        val mockResponseJson = adapter.toJson(
            getLatestRatesResponse(
                source = source,
                avableCurrencies = avableCurrencies,
                currencies = currencies
            )
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)

        )
        val request = getLatestRatesRequest().copy(
            source = source,
            currencies = currencies
        )
        val responde = repository.getLatestRates(request)
        assertThat(responde).isInstanceOf(Result::class)
        assertThat(responde.isSuccess).isTrue()
        assertThat(responde.getOrNull()).isNotNull()
        val key = responde.getOrNull()!!.quotes!!.keys.first()
        assertThat(currencies).isEqualTo(key)

    }


    @Test
    fun `Make a request for currencies, get error response`() = runBlocking<Unit> {
        val adapter = moshi.adapter(GetCurrencies.Response::class.java)
        val mockResponse = getCurrenciesResponse().copy(success = false)
        val mockResponseJson = adapter.toJson(mockResponse)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)
        )
        val result = repository.getCurrencies()
        assertThat(result.isFailure).isTrue()
        val message = result.exceptionOrNull()?.message
        assertThat(message).isNotNull()
        assertThat(message!!).isNotEmpty()
    }

    @Test
    fun `Make a request for rates, get error response`() = runBlocking {
        val avableCurrencies = getCurrencies()
        val source = avableCurrencies.first().code
        val currencies = avableCurrencies.last().code
        val adapter = moshi.adapter(GetLatestRates.Response::class.java)
        val mockResponse = getLatestRatesResponse(
            source = source,
            avableCurrencies = avableCurrencies,
            currencies = currencies
        ).copy(success = false)
        val mockResponseJson = adapter.toJson(mockResponse)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)

        )
        val result = repository.getLatestRates(getLatestRatesRequest())
        assertThat(result.isFailure).isTrue()
        val message = result.exceptionOrNull()?.message
        assertThat(message).isNotNull()
        assertThat(message!!).isNotEmpty()

    }


}