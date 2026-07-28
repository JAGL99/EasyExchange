package com.jagl.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jagl.data.local.entity.CurrencyEntity

/**
 * DAO to access currencies in the database
 */
@Dao
interface CurrencyDao {

    /**
     * Inserts a currency into the database.
     * If it already exists, it is replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(exchangeRate: CurrencyEntity)

    /**
     * Inserts a list of currencies into the database.
     * If they already exist, they are replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(exchangeRates: List<CurrencyEntity>)

    /**
     * Gets all currencies
     */
    @Query("SELECT * FROM currencies")
    suspend fun getCurrencies(): List<CurrencyEntity>

    /**
     * Gets a specific currency by its code
     */
    @Query("SELECT * FROM currencies WHERE code = :code LIMIT 1")
    suspend fun getCurrencyByCode(code: String): CurrencyEntity?

}