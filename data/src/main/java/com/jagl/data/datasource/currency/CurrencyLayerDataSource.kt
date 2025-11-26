package com.jagl.data.datasource.currency

import com.jagl.core.network.INetworkManager
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.api.utils.ApiUtils
import com.jagl.data.local.dao.CurrencyDao
import com.jagl.data.local.entity.CurrencyEntity
import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repositorio que maneja las operaciones relacionadas con las tasas de cambio
 */
class CurrencyLayerDataSource @Inject constructor(
    private val networkManager: INetworkManager,
    private val api: ICurrencyLayerRepository,
    private val currencyDao: CurrencyDao,
) : ICurrencyDataSource {

    /**
     * Obtiene la lista de monedas disponibles
     */
    override suspend fun getAvailableCurrencies(): ApiState<List<Currency>> = withContext(Dispatchers.Default) {

            val localDate = currencyDao.getCurrencies().map { it.toCurrency() }

            if (!localDate.isNullOrEmpty()) {
                return@withContext ApiState.Success(localDate)
            }

            if (networkManager.isConnected().not()) {
                return@withContext ApiState.Error(message = ApiUtils.CONNECTION_ERROR)
            }

            val result = api.getCurrencies()


            if (result.isFailure){
                val exception = result.exceptionOrNull()
                val message = exception?.message.orEmpty()
                return@withContext ApiState.Error(exception , message)
            }

            val currencies = result.getOrThrow().currencies ?: emptyMap()
            val currencyList = currencies.map { Currency(code = it.key, name = it.value) }
            currencyDao.insertCurrencies(currencyList.map(CurrencyEntity.Companion::fromCurrency))
            return@withContext ApiState.Success(currencyList)
        }

}