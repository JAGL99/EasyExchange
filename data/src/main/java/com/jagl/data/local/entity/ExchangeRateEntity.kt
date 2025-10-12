package com.jagl.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jagl.domain.model.ExchangeRate

/**
 * Entidad para almacenar las tasas de cambio en la base de datos local
 */
@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val timestamp: Long,
    val date: String
) {
    /**
     * Convierte la entidad a un modelo de dominio
     */
    fun toExchangeRate(): ExchangeRate {
        return ExchangeRate(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            rate = rate
        )
    }

    companion object {
        /**
         * Crea una entidad a partir de un modelo de dominio
         */
        fun fromExchangeRate(
            exchangeRate: ExchangeRate,
            timestamp: Long,
            date: String
        ): ExchangeRateEntity {
            return ExchangeRateEntity(
                id = 0,
                fromCurrency = exchangeRate.fromCurrency,
                toCurrency = exchangeRate.toCurrency,
                rate = exchangeRate.rate,
                timestamp = timestamp,
                date = date
            )
        }
    }
}