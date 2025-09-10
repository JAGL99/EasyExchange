package com.jagl.exchangeapp.data.repository

import android.content.Context
import com.jagl.exchangeapp.data.api.ExchangeRateApi
import com.jagl.exchangeapp.data.local.ExchangeDatabase
import com.jagl.exchangeapp.data.local.entity.ExchangeRateEntity
import com.jagl.exchangeapp.data.model.Currency
import com.jagl.exchangeapp.data.model.CurrencyData
import com.jagl.exchangeapp.data.model.ExchangeRate
import com.jagl.exchangeapp.util.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio que maneja las operaciones relacionadas con las tasas de cambio
 */
@Singleton
class ExchangeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ExchangeRateApi,
    private val database: ExchangeDatabase
) {
    private val exchangeRateDao = database.exchangeRateDao()
    
    /**
     * Obtiene la lista de monedas disponibles
     */
    fun getAvailableCurrencies(): List<Currency> {
        return CurrencyData.availableCurrencies
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
                val hasDataForToday = exchangeRateDao.hasExchangeRatesForDate(baseCurrency, currentDate) > 0
                
                if (hasDataForToday) {
                    // Usar datos locales
                    val localRates = exchangeRateDao.getExchangeRatesForDate(baseCurrency, currentDate)
                    val rates = localRates.map { it.toExchangeRate() }
                    return@withContext Result.success(rates)
                }
                
                // Si no hay datos locales para hoy, obtenerlos de la API
                // Obtenemos todas las monedas disponibles excepto la base
                val targetCurrencies = CurrencyData.availableCurrencies
                    .filter { it.code != baseCurrency }.joinToString(",") { it.code }

                val response = api.getLatestRates(baseCurrency, targetCurrencies)
                if (response.success) {
                    val rates = response.quotes.map { (quotePair, rate) ->
                        // El formato de la clave es USDEUR (moneda base + moneda destino)
                        val toCurrency = quotePair.substring(baseCurrency.length)
                        ExchangeRate(
                            fromCurrency = baseCurrency,
                            toCurrency = toCurrency,
                            rate = rate
                        )
                    }
                    
                    // Guardar en la base de datos local
                    val entities = rates.map { rate ->
                        ExchangeRateEntity.fromExchangeRate(
                            exchangeRate = rate,
                            date = currentDate,
                            source = baseCurrency
                        )
                    }
                    exchangeRateDao.insertExchangeRates(entities)
                    
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
                val localRate = exchangeRateDao.getExchangeRateForDate(fromCurrency, toCurrency, currentDate)
                
                if (localRate != null) {
                    // Usar datos locales
                    return@withContext Result.success(amount * localRate.rate)
                }
                
                // Si no hay datos locales para hoy, obtenerlos de la API
                val response = api.getLatestRates(fromCurrency, toCurrency)
                if (response.success) {
                    // La clave en el mapa quotes tiene el formato USDEUR (fromCurrency + toCurrency)
                    val quoteKey = "${fromCurrency}${toCurrency}"
                    val rate = response.quotes[quoteKey]
                    if (rate != null) {
                        // Guardar en la base de datos local
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