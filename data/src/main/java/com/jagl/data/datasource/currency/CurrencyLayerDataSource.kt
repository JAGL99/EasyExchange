package com.jagl.data.datasource.currency

import com.jagl.data.api.model.GetCurrencies
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.datasource.currency.ICurrencyDataSource
import com.jagl.data.local.dao.CurrencyDao
import com.jagl.data.local.entity.CurrencyEntity
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
    private val currencyDao: CurrencyDao
) : ICurrencyDataSource {

    /**
     * Obtiene la lista de monedas disponibles
     */
    override suspend fun getAvailableCurrencies(): List<Currency> =
        withContext(Dispatchers.Default) {

            if (networkManager.isConnected().not()) {
                return@withContext emptyList()
            }
            val request = GetCurrencies.Request()
            val result = api.getCurrencies(request)



            if (result.isFailure || result.getOrNull() == null) {
                return@withContext currencyDao.getCurrencies().map { it.toCurrency() }
            }

            val currencies = result.getOrThrow().currencies ?: emptyMap()
            val currencyList = currencies.map { Currency(code = it.key, name = it.value) }
            currencyDao.insertCurrencies(currencyList.map(CurrencyEntity.Companion::fromCurrency))
            return@withContext currencyList
        }

}