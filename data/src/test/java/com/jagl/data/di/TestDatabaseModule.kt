package com.jagl.data.di

import android.content.Context
import androidx.room.Room
import com.jagl.data.local.database.ExchangeDatabase
import com.jagl.data.local.database.IExchangeDatabase
import com.jagl.data.local.dao.CurrencyDao
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
object TestDatabaseModule {

    /**
     * Proporciona una instancia de la base de datos Room
     */
    @Provides
    @Singleton
    fun provideExchangeDatabase(@ApplicationContext context: Context): IExchangeDatabase {
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
    fun provideExchangeRateDao(database: IExchangeDatabase): ExchangeRateDao {
        return database.exchangeRateDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(database: IExchangeDatabase): CurrencyDao {
        return database.currencyDao()
    }
}