package com.jagl.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jagl.data.local.entity.ExchangeRateEntity

/**
 * DAO to access exchange rates in the database
 */
@Dao
interface ExchangeRateDao {

    /**
     * Inserts an exchange rate into the database.
     * If it already exists, it's replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(exchangeRate: ExchangeRateEntity)

    /**
     * Inserts a list of exchange rates into the database.
     * If they already exist, they are replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(exchangeRates: List<ExchangeRateEntity>)

    /**
     * Gets a specific exchange rate for a date
     */
    @Query("SELECT * FROM exchange_rates WHERE fromCurrency = :fromCurrency AND toCurrency = :toCurrency AND date = :date ORDER BY date ASC , timestamp ASC")
    suspend fun getExchangeRateForDate(
        fromCurrency: String,
        toCurrency: String,
        date: String
    ): List<ExchangeRateEntity>

    @Delete
    suspend fun deleteExchangeRate(exchangeRate: ExchangeRateEntity)
}