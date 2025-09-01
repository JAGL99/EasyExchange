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
                val response = api.getLatestRates(baseCurrency)
                if (response.success) {
                    val rates = response.rates.map { (currencyCode, rate) ->
                        ExchangeRate(
                            fromCurrency = baseCurrency,
                            toCurrency = currencyCode,
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
                val response = api.getLatestRates(fromCurrency)
                if (response.success) {
                    val rate = response.rates[toCurrency]
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