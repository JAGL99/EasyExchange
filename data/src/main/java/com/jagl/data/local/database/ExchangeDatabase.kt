package com.jagl.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jagl.data.local.dao.CurrencyDao
import com.jagl.data.local.dao.ExchangeRateDao
import com.jagl.data.local.entity.CurrencyEntity
import com.jagl.data.local.entity.ExchangeRateEntity

/**
 * Base de datos de Room para la aplicación
 */
@Database(
    entities = [
        ExchangeRateEntity::class,
        CurrencyEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class ExchangeDatabase : RoomDatabase(), IExchangeDatabase {

    /**
     * DAO para acceder a las tasas de cambio
     */
    abstract override fun exchangeRateDao(): ExchangeRateDao

    abstract override fun currencyDao(): CurrencyDao
}