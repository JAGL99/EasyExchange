package com.jagl.data.datasource.exchangeRate


import com.jagl.core.network.INetworkManager
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.api.utils.ApiUtils
import com.jagl.data.local.dao.ExchangeRateDao
import com.jagl.data.local.entity.ExchangeRateEntity
import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency
import com.jagl.domain.model.ExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repositorio que maneja las operaciones relacionadas con las tasas de cambio
 */
class ExchangeDataSource @Inject constructor(
    private val networkManager: INetworkManager,
    private val api: ICurrencyLayerRepository,
    private val exchangeRateDao: ExchangeRateDao,
) : IExchangeDataSource {

    /**
     * Convierte un monto de una moneda a otra
     * @param amount Monto a convertir
     * @param fromCurrency Moneda de origen
     * @param toCurrency Moneda de destino
     * @return Monto convertido
     */
    override suspend fun getExchangeRate(
        amount: Double,
        date: String,
        fromCurrency: Currency,
        toCurrency: Currency
    ): ApiState<ExchangeRate> {
        return withContext(Dispatchers.IO) {
            try {
                val localRate =
                    exchangeRateDao.getExchangeRateForDate(fromCurrency.code, toCurrency.code, date)

                if (localRate.isNotEmpty()) {
                    val rate = amount * localRate.first().rate
                    val exchangeRate = ExchangeRate(
                        fromCurrency = fromCurrency.code,
                        toCurrency = toCurrency.code,
                        rate = rate
                    )
                    return@withContext ApiState.Success(exchangeRate)
                }

                if (networkManager.isConnected().not()) {
                    return@withContext ApiState.Error(message = ApiUtils.CONNECTION_ERROR)
                }

                val request = GetLatestRates.Request(
                    source = fromCurrency.code,
                    currencies = toCurrency.code
                )

                val result = api.getLatestRates(request)

                if (result.isFailure) {
                    val exception = result.exceptionOrNull()
                    val message = exception?.message.orEmpty()
                    return@withContext ApiState.Error(exception, message)
                }

                val body = result.getOrThrow()
                val rate = body.quotes?.get(toCurrency.code)
                if (rate == null) return@withContext ApiState.Error( message = "Invalid rate")

                val exchangeRate = ExchangeRate(
                    fromCurrency = fromCurrency.code,
                    toCurrency = toCurrency.code,
                    rate = rate
                )
                val entity = ExchangeRateEntity.fromExchangeRate(
                    exchangeRate = exchangeRate,
                    date = date,
                    timestamp = body.timestamp ?: 0
                )
                exchangeRateDao.insertExchangeRate(entity)
                return@withContext ApiState.Success(exchangeRate)
            } catch (e: Exception) {
                return@withContext ApiState.Error(e, e.message.orEmpty())
            }
        }
    }
}