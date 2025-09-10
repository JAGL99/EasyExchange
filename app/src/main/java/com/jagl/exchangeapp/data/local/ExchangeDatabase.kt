package com.jagl.exchangeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jagl.exchangeapp.data.local.dao.ExchangeRateDao
import com.jagl.exchangeapp.data.local.entity.ExchangeRateEntity

/**
 * Base de datos de Room para la aplicación
 */
@Database(entities = [ExchangeRateEntity::class], version = 1, exportSchema = false)
abstract class ExchangeDatabase : RoomDatabase() {

    /**
     * DAO para acceder a las tasas de cambio
     */
    abstract fun exchangeRateDao(): ExchangeRateDao
}