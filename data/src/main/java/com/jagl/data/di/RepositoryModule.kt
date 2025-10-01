package com.jagl.data.di

import com.jagl.data.api.client.CurrencyLayerApi
import com.jagl.data.api.repository.CurrencyLayerRepositoryImpl
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.datasource.CurrencyLayerDataSource
import com.jagl.data.local.ExchangeDatabase
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
    fun provideCurrencyLayerDataSource(
        currencyRepository: ICurrencyLayerRepository,
        database: ExchangeDatabase
    ): CurrencyLayerDataSource {
        return CurrencyLayerDataSource(currencyRepository, database)
    }


    @Provides
    @Singleton
    fun provideCurrencyLayerRepository(api: CurrencyLayerApi): ICurrencyLayerRepository {
        return CurrencyLayerRepositoryImpl(api)
    }

}