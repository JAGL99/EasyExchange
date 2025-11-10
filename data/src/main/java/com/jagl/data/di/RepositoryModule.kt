package com.jagl.data.di

import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.datasource.currency.CurrencyLayerDataSource
import com.jagl.data.datasource.exchangeRate.ExchangeDataSource
import com.jagl.data.datasource.currency.ICurrencyDataSource
import com.jagl.data.datasource.exchangeRate.IExchangeDataSource
import com.jagl.data.local.database.IExchangeDatabase
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
        networkManager: INetworkManager,
        currencyRepository: ICurrencyLayerRepository,
        database: IExchangeDatabase
    ): ICurrencyDataSource {
        val dao = database.currencyDao()
        return CurrencyLayerDataSource(networkManager,currencyRepository, dao)
    }

    @Provides
    @Singleton
    fun provideExchangerDataSource(
        networkManager: INetworkManager,
        currencyRepository: ICurrencyLayerRepository,
        database: IExchangeDatabase
    ): IExchangeDataSource {
        val dao = database.exchangeRateDao()
        return ExchangeDataSource(networkManager,currencyRepository, dao)
    }

}