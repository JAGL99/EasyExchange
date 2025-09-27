package com.jagl.data.di

import android.content.Context
import androidx.room.Room
import com.jagl.data.local.ExchangeDatabase
import com.jagl.data.local.dao.ExchangeRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt que proporciona las dependencias de la base de datos
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Proporciona una instancia de la base de datos Room
     */
    @Provides
    @Singleton
    fun provideExchangeDatabase(@ApplicationContext context: Context): ExchangeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ExchangeDatabase::class.java,
            "exchange_database"
        ).build()
    }

    /**
     * Proporciona el DAO para acceder a las tasas de cambio
     */
    @Provides
    @Singleton
    fun provideExchangeRateDao(database: ExchangeDatabase): ExchangeRateDao {
        return database.exchangeRateDao()
    }
}