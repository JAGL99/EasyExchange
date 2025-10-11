package com.jagl.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jagl.data.local.entity.ExchangeRateEntity

/**
 * DAO para acceder a las tasas de cambio en la base de datos
 */
@Dao
interface ExchangeRateDao {

    /**
     * Inserta una tasa de cambio en la base de datos
     * Si ya existe, la reemplaza
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(exchangeRate: ExchangeRateEntity)

    /**
     * Inserta una lista de tasas de cambio en la base de datos
     * Si ya existen, las reemplaza
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(exchangeRates: List<ExchangeRateEntity>)

    /**
     * Obtiene una tasa de cambio específica para una fecha
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