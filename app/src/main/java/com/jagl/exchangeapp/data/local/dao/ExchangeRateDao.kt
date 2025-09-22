package com.jagl.exchangeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jagl.exchangeapp.data.local.entity.ExchangeRateEntity

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
     * Obtiene todas las tasas de cambio para una moneda base y una fecha específica
     */
    @Query("SELECT * FROM exchange_rates WHERE fromCurrency = :baseCurrency AND date = :date")
    suspend fun getExchangeRatesForDate(
        baseCurrency: String,
        date: String
    ): List<ExchangeRateEntity>

    /**
     * Obtiene una tasa de cambio específica para una fecha
     */
    @Query("SELECT * FROM exchange_rates WHERE fromCurrency = :fromCurrency AND toCurrency = :toCurrency AND date = :date LIMIT 1")
    suspend fun getExchangeRateForDate(
        fromCurrency: String,
        toCurrency: String,
        date: String
    ): ExchangeRateEntity?

    /**
     * Verifica si existen tasas de cambio para una moneda base y una fecha específica
     */
    @Query("SELECT COUNT(*) FROM exchange_rates WHERE fromCurrency = :baseCurrency AND date = :date")
    suspend fun hasExchangeRatesForDate(baseCurrency: String, date: String): Int
}