package com.jagl.data.datasource.exchangeRate


import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.local.dao.ExchangeRateDao
import com.jagl.data.local.entity.ExchangeRateEntity
import com.jagl.domain.model.ExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repositorio que maneja las operaciones relacionadas con las tasas de cambio
 */
class ExchangeDataSource @Inject constructor(
    private val api: ICurrencyLayerRepository,
    private val exchangeRateDao: ExchangeRateDao
) : IExchangeDataSource {

    /**
     * Convierte un monto de una moneda a otra
     * @param amount Monto a convertir
     * @param fromCurrency Moneda de origen
     * @param toCurrency Moneda de destino
     * @return Monto convertido
     */
    override suspend fun convertCurrency(
        amount: Double,
        date: String,
        fromCurrency: String,
        toCurrency: String
    ): Result<Double> {
        return withContext(Dispatchers.IO) {
            try {

                // Verificar si ya tenemos la tasa de cambio para hoy
                val localRate =
                    exchangeRateDao.getExchangeRateForDate(fromCurrency, toCurrency, date)

                if (localRate.isNotEmpty()) {
                    // Usar datos locales
                    return@withContext Result.success(amount * localRate.first().rate)
                }

                val request = GetLatestRates.Request(
                    source = fromCurrency,
                    currencies = toCurrency
                )
                val response = api.getLatestRates(request)
                if (response.isSuccess) {
                    val body = response.getOrThrow()
                    val rate = body.quotes?.get(toCurrency)
                    if (rate != null) {
                        val exchangeRate = ExchangeRate(
                            fromCurrency = fromCurrency,
                            toCurrency = toCurrency,
                            rate = rate
                        )
                        val entity = ExchangeRateEntity.fromExchangeRate(
                            exchangeRate = exchangeRate,
                            date = date,
                            timestamp = body.timestamp ?: 0
                        )

                        exchangeRateDao.insertExchangeRate(entity)

                        return@withContext Result.success(amount * rate)
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