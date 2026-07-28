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
        val localData = currencyDao.getCurrencies().map { it.toCurrency() }

        if (localData.isNotEmpty()) {
            return@safeApiStateCall ApiState.Success(localData)
        }

        if (networkManager.isConnected().not())
            return@safeApiStateCall ApiState.Error(ApiUtils.NO_INTERNET_ERROR)


        val result = api.getCurrencies()

        if (result.isFailure) {
            val message = result.exceptionOrNull()?.message ?: ApiUtils.GENERIC_ERROR
            return@safeApiStateCall ApiState.Error(message)
        }

        val currencies = result.getOrThrow().currencies ?: emptyMap()
        val currencyList = currencies.map { Currency(code = it.key, name = it.value) }
        currencyDao.insertCurrencies(currencyList.map(CurrencyEntity.Companion::fromCurrency))
        return@safeApiStateCall ApiState.Success(currencyList)
    }

}