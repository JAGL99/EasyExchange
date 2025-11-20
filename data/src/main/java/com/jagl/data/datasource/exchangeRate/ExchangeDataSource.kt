package com.jagl.data.datasource.exchangeRate


import com.jagl.core.network.INetworkManager
import com.jagl.core.preferences.SharedPrefManager
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.local.dao.ExchangeRateDao
import com.jagl.data.local.entity.ExchangeRateEntity
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
    private val prefManager: SharedPrefManager
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
    ): Result<ExchangeRate> {
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
                    return@withContext Result.success(exchangeRate)
                }

                if (networkManager.isConnected().not()) {
                    return@withContext Result.failure(Exception("Sin conexión a internet"))
                }
                val token =  prefManager.getString("TOKEN", "").orEmpty()
                val request = GetLatestRates.Request(
                    source = fromCurrency.code,
                    currencies = toCurrency.code,
                    accessKey = token
                )

                val response = api.getLatestRates(request)
                if (response.isSuccess) {
                    val body = response.getOrThrow()
                    val rate = body.quotes?.get(toCurrency.code)
                    if (rate != null) {
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

                        return@withContext Result.success(exchangeRate)
                    } else {
                        return@withContext Result.failure(Exception("Moneda de destino no encontrada"))
                    }
                } else {
                    return@withContext Result.failure(Exception("Error al obtener tasas de cambio"))
                }
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }
}