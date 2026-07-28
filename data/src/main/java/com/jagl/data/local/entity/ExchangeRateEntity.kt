package com.jagl.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jagl.domain.model.ExchangeRate

/**
 * Entity for storing exchange rates in the local database
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
     * Converts the entity to a domain model
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
         * Creates an entity from a domain model
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