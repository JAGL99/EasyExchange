package com.jagl.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jagl.domain.model.ExchangeRate
import java.util.Date

/**
 * Entidad para almacenar las tasas de cambio en la base de datos local
 */
@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey
    val id: String, // Formato: "fromCurrency_toCurrency"
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val timestamp: Long,
    val date: String, // Fecha en formato YYYY-MM-DD
    val source: String
) {
    /**
     * Convierte la entidad a un modelo de dominio
     */
    fun toExchangeRate(): ExchangeRate {
        return ExchangeRate(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            rate = rate,
            timestamp = Date(timestamp)
        )
    }

    companion object {
        /**
         * Crea una entidad a partir de un modelo de dominio
         */
        fun fromExchangeRate(
            exchangeRate: ExchangeRate,
            date: String,
            source: String
        ): ExchangeRateEntity {
            return ExchangeRateEntity(
                id = "${exchangeRate.fromCurrency}_${exchangeRate.toCurrency}",
                fromCurrency = exchangeRate.fromCurrency,
                toCurrency = exchangeRate.toCurrency,
                rate = exchangeRate.rate,
                timestamp = exchangeRate.timestamp.time,
                date = date,
                source = source
            )
        }
    }
}