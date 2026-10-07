package com.jagl.data.datasource.currency

import com.jagl.core.network.INetworkManager
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.api.utils.ApiUtils
import com.jagl.data.api.utils.ApiUtils.safeApiStateCall
import com.jagl.data.local.dao.CurrencyDao
import com.jagl.data.local.entity.CurrencyEntity
import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency
import javax.inject.Inject

/**
 * Data source that handles operations related to currencies.
 */
class CurrencyLayerDataSource @Inject constructor(
    private val networkManager: INetworkManager,
    private val api: ICurrencyLayerRepository,
    private val currencyDao: CurrencyDao,
) : ICurrencyDataSource {

    /**
     * Gets the list of available currencies.
     * It first checks the local database. If it's empty, it fetches the data from the API
     * and stores it in the local database.
     * @return An [ApiState] with a list of [Currency] objects.
     */
    override suspend fun getAvailableCurrencies(): ApiState<List<Currency>> = safeApiStateCall {
        println("CurrencyLayerDataSource: getAvailableCurrencies called")
        val localData = currencyDao.getCurrencies().map { it.toCurrency() }

        if (localData.isNotEmpty()) {
            println("CurrencyLayerDataSource: Returning local data with ${localData.size} currencies")
            return@safeApiStateCall ApiState.Success(localData)
        }

        println("CurrencyLayerDataSource: Local data is empty, fetching from API")

        if (networkManager.isConnected().not())
            return@safeApiStateCall ApiState.Error(ApiUtils.NO_INTERNET_ERROR)

        println("CurrencyLayerDataSource: Network is connected, calling API")

        val result = api.getCurrencies()

        println("CurrencyLayerDataSource: API call completed with result: $result")

        if (result.isFailure) {
            println("CurrencyLayerDataSource: API call failed with exception: ${result.exceptionOrNull()}")
            val message = result.exceptionOrNull()?.message ?: ApiUtils.GENERIC_ERROR
            return@safeApiStateCall ApiState.Error(message)
        }
        println("CurrencyLayerDataSource: Mapping API response to Currency objects")
        val currencyList = result.getOrThrow().map {
            println("CurrencyLayerDataSource: Mapping CurrencyDto to Currency: code=${it.iso_code}, name=${it.name}")
            Currency(code = it.iso_code.orEmpty(), name = it.name.orEmpty())
        }
        println("CurrencyLayerDataSource: Inserting ${currencyList.size} currencies into local database")
        currencyDao.insertCurrencies(currencyList.map(CurrencyEntity.Companion::fromCurrency))
        println("CurrencyLayerDataSource: Returning API data with ${currencyList.size} currencies")
        return@safeApiStateCall ApiState.Success(currencyList)
    }

}