package com.jagl.data.test.repository

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.corresponds
import assertk.assertions.hasClass
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.jagl.core.network.INetworkManager
import com.jagl.core.network.NetworkStatus
import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.model.getCurrenciesResponse
import com.jagl.data.api.repository.CurrencyLayerRepositoryImpl
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.api.utils.ApiUtils
import com.jagl.data.api.utils.toCurrencyList
import com.jagl.data.datasource.currency.CurrencyLayerDataSource
import com.jagl.data.datasource.currency.ICurrencyDataSource
import com.jagl.data.local.CurrencyDaoFake
import com.jagl.data.local.dao.CurrencyDao
import com.jagl.domain.model.ApiState
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
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class CurrencyDataSourceTest {

    private lateinit var dao: CurrencyDao
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi
    private lateinit var repository: ICurrencyLayerRepository
    private lateinit var dataSource: ICurrencyDataSource

    private lateinit var networkManager: INetworkManager

    @BeforeEach
    fun setUp() {
        dao = CurrencyDaoFake()
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
        dataSource = CurrencyLayerDataSource(networkManager,repository, dao)
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
        dataSource = CurrencyLayerDataSource(networkManager,repository, dao)
        val apiState = dataSource.getAvailableCurrencies()
        assertThat(apiState).hasClass(ApiState.Error::class)
        val error = (apiState as ApiState.Error)
        assertThat(error.message).isEqualTo(ApiUtils.NO_INTERNET_ERROR)
        val localData = dao.getCurrencies()
        assertThat(localData).isEmpty()
    }

    @Test
    fun `Request bad list, get empty data`() = runBlocking<Unit> {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val apiState = dataSource.getAvailableCurrencies()
        assertThat(apiState).hasClass(ApiState.Error::class)
        val localData = dao.getCurrencies()
        assertThat(localData).isEmpty()
    }

    @Test
    fun `Request list two times, get success but withour repetition`() = runBlocking<Unit> {
        val mockResponse = getCurrenciesResponse()
        val mockCurrencies = mockResponse.currencies!!.toCurrencyList().toTypedArray()
        val adapter = moshi.adapter(GetCurrencies.Response::class.java)
        val mockResponseJson = adapter.toJson(mockResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)

        )
        val firstApiResult = dataSource.getAvailableCurrencies()
        assertThat(firstApiResult).hasClass(ApiState.Success::class)
        val firstResponse = (firstApiResult as ApiState.Success)

        assertThat(firstResponse.data).isNotEmpty()
        val firstLocalData = dao.getCurrencies().map { it.toCurrency() }
        assertThat(firstLocalData).containsExactly(*mockCurrencies)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)

        )
        val secondApiResult = dataSource.getAvailableCurrencies()
        assertThat(secondApiResult).hasClass(ApiState.Success::class)
        val secondLocalData = dao.getCurrencies().map { it.toCurrency() }
        assertThat(secondLocalData).containsExactly(*mockCurrencies)

        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val thirdApiResult = dataSource.getAvailableCurrencies()
        assertThat(thirdApiResult).hasClass(ApiState.Success::class)
        val thirdLocalData = dao.getCurrencies().map { it.toCurrency() }
        assertThat(thirdLocalData).containsExactly(*mockCurrencies)
    }

    @Test
    fun `Request list, get success with data`() = runBlocking<Unit> {
        val mockResponse = getCurrenciesResponse()
        val mockCurrencies = mockResponse.currencies!!.toCurrencyList().toTypedArray()
        val adapter = moshi.adapter(GetCurrencies.Response::class.java)
        val mockResponseJson = adapter.toJson(mockResponse)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseJson)

        )
        val apiResult = dataSource.getAvailableCurrencies()

        assertThat(apiResult).hasClass(ApiState.Success::class)
        val responseData = (apiResult as ApiState.Success).data
        assertThat(responseData).containsExactly(*mockCurrencies)
        val localData = dao.getCurrencies().map { it.toCurrency() }
        assertThat(localData).containsExactly(*mockCurrencies)
    }


}