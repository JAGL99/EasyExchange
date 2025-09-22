package com.jagl.exchangeapp.di

import com.jagl.exchangeapp.data.api.ExchangeRateApi
import com.jagl.exchangeapp.data.local.ExchangeDatabase
import com.jagl.exchangeapp.data.repository.ExchangeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt que proporciona el repositorio para la aplicación
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    // La base de datos ahora se proporciona desde DatabaseModule
    
    /**
     * Proporciona una instancia del repositorio de tasas de cambio
     */
    @Provides
    @Singleton
    fun provideExchangeRepository(
        exchangeRateApi: ExchangeRateApi,
        database: ExchangeDatabase
    ): ExchangeRepository {
        return ExchangeRepository(exchangeRateApi, database)
    }
}