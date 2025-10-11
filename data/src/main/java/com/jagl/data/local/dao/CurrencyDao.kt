package com.jagl.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jagl.data.local.entity.CurrencyEntity

/**
 * DAO para acceder a las tasas de cambio en la base de datos
 */
@Dao
interface CurrencyDao {

    /**
     * Inserta una tasa de cambio en la base de datos
     * Si ya existe, la reemplaza
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(exchangeRate: CurrencyEntity)

    /**
     * Inserta una lista de tasas de cambio en la base de datos
     * Si ya existen, las reemplaza
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(exchangeRates: List<CurrencyEntity>)

    /**
     * Obtiene todas las divisas
     */
    @Query("SELECT * FROM currencies")
    suspend fun getCurrencies(): List<CurrencyEntity>

    /**
     * Obtiene una tasa de cambio específica para una fecha
     */
    @Query("SELECT * FROM currencies WHERE code = :code LIMIT 1")
    suspend fun getCurrencyByCode(code: String): CurrencyEntity?

}