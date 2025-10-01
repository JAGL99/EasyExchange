package com.jagl.data.datasource


import com.jagl.core.util.DateUtils
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.local.ExchangeDatabase
import com.jagl.data.local.entity.ExchangeRateEntity
import com.jagl.domain.model.Currency
import com.jagl.domain.model.ExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repositorio que maneja las operaciones relacionadas con las tasas de cambio
 */
class CurrencyLayerDataSource @Inject constructor(
    private val api: ICurrencyLayerRepository,
    private val database: ExchangeDatabase
) {
    private val exchangeRateDao = database.exchangeRateDao()


    /**
     * Obtiene la lista de monedas disponibles
     */
    fun getAvailableCurrencies(): List<Currency> {
        return emptyList()//CurrencyData.availableCurrencies
    }


    /**
     * Obtiene las tasas de cambio para una moneda base
     * @param baseCurrency Código de la moneda base
     * @return Lista de tasas de cambio
     */
    suspend fun getExchangeRates(baseCurrency: String): Result<List<ExchangeRate>> {
        return withContext(Dispatchers.IO) {
            try {
                val currentDate = DateUtils.getCurrentDate()

                // Verificar si ya tenemos datos para hoy
                val hasDataForToday =
                    exchangeRateDao.hasExchangeRatesForDate(baseCurrency, currentDate) > 0

                if (hasDataForToday) {
                    // Usar datos locales
                    val localRates =
                        exchangeRateDao.getExchangeRatesForDate(baseCurrency, currentDate)
                    val rates = localRates.map { it.toExchangeRate() }
                    return@withContext Result.success(rates)
                }

                // Si no hay datos locales para hoy, obtenerlos de la API
                // Obtenemos todas las monedas disponibles excepto la base
                val targetCurrencies = emptyList<Currency>()//CurrencyData.availableCurrencies
                    .filter { it.code != baseCurrency }.joinToString(",") { it.code }

                val request = GetLatestRates.Request(
                    source = baseCurrency,
                    currencies = targetCurrencies
                )
                val response = api.getLatestRates(request)
                if (response.isSuccess) {
                    val rates = response
                        .getOrThrow()
                        .quotes?.map { (quotePair, rate) ->
                            ExchangeRate(
                                fromCurrency = baseCurrency,
                                toCurrency = quotePair,
                                rate = rate
                            )
                        } ?: emptyList()

                    // Guardar en la base de datos local
                    rates?.map { rate ->
                        ExchangeRateEntity.fromExchangeRate(
                            exchangeRate = rate,
                            date = currentDate,
                            source = baseCurrency
                        )
                    }?.let { exchangeRateDao.insertExchangeRates(it) }

                    return@withContext Result.success(rates)
                } else {
                    return@withContext Result.failure(Exception("Error al obtener tasas de cambio"))
                }
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    /**
     * Convierte un monto de una moneda a otra
     * @param amount Monto a convertir
     * @param fromCurrency Moneda de origen
     * @param toCurrency Moneda de destino
     * @return Monto convertido
     */
    suspend fun convertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String
    ): Result<Double> {
        return withContext(Dispatchers.IO) {
            try {
                val currentDate = DateUtils.getCurrentDate()

                // Verificar si ya tenemos la tasa de cambio para hoy
                val localRate =
                    exchangeRateDao.getExchangeRateForDate(fromCurrency, toCurrency, currentDate)

                if (localRate != null) {
                    // Usar datos locales
                    return@withContext Result.success(amount * localRate.rate)
                }

                val request = GetLatestRates.Request(
                    source = fromCurrency,
                    currencies = toCurrency
                )
                val response = api.getLatestRates(request)
                if (response.isSuccess) {
                    val rate = response.getOrThrow().quotes?.get(toCurrency)
                    if (rate != null) {
                        val exchangeRate = ExchangeRate(
                            fromCurrency = fromCurrency,
                            toCurrency = toCurrency,
                            rate = rate
                        )
                        val entity = ExchangeRateEntity.fromExchangeRate(
                            exchangeRate = exchangeRate,
                            date = currentDate,
                            source = fromCurrency
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