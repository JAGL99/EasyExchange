package com.jagl.exchangeapp.data.repository

import com.jagl.exchangeapp.data.api.NetworkModule
import com.jagl.exchangeapp.data.model.Currency
import com.jagl.exchangeapp.data.model.CurrencyData
import com.jagl.exchangeapp.data.model.ExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio que maneja las operaciones relacionadas con las tasas de cambio
 */
class ExchangeRepository {
    private val api = NetworkModule.exchangeRateApi
    
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
                // Obtenemos todas las monedas disponibles excepto la base
                val targetCurrencies = CurrencyData.availableCurrencies
                    .filter { it.code != baseCurrency }
                    .map { it.code }
                    .joinToString(",")
                
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
                    Result.success(rates)
                } else {
                    Result.failure(Exception("Error al obtener tasas de cambio"))
                }
            } catch (e: Exception) {
                Result.failure(e)
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
                // Para la API de apilayer, necesitamos solicitar específicamente la moneda de destino
                val response = api.getLatestRates(fromCurrency, toCurrency)
                if (response.success) {
                    // La clave en el mapa quotes tiene el formato USDEUR (fromCurrency + toCurrency)
                    val quoteKey = "${fromCurrency}${toCurrency}"
                    val rate = response.quotes[quoteKey]
                    if (rate != null) {
                        Result.success(amount * rate)
                    } else {
                        Result.failure(Exception("Moneda de destino no encontrada"))
                    }
                } else {
                    Result.failure(Exception("Error al obtener tasas de cambio"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}