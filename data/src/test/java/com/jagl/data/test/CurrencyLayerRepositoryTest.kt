package com.jagl.data.test


import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.model.getCurrencies
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


    @Test
    fun `Request bad rates, get failure with no data`() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val request = getLatestRatesRequest()
        val responde = repository.getLatestRates(request)
        assertThat(responde.isFailure).isTrue()
    }

    @Test
    fun `should return currency data when API call is successful`() = runBlocking {
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

}




