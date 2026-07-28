package com.jagl.data.di

import com.jagl.core.network.INetworkManager
import com.jagl.core.preferences.SharedPrefManager
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.datasource.currency.CurrencyLayerDataSource
import com.jagl.data.datasource.currency.ICurrencyDataSource
import com.jagl.data.datasource.exchangeRate.ExchangeDataSource
import com.jagl.data.datasource.exchangeRate.IExchangeDataSource
import com.jagl.data.local.database.IExchangeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides the repository for the application
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // The database is now provided from DatabaseModule

    /**
     * Provides an instance of the exchange rate repository
     */
    @Provides
    @Singleton
    fun provideCurrencyLayerDataSource(
        networkManager: INetworkManager,
        currencyRepository: ICurrencyLayerRepository,
        database: IExchangeDatabase
    ): ICurrencyDataSource {
        val dao = database.currencyDao()
        return CurrencyLayerDataSource(networkManager, currencyRepository, dao)
    }

    @Provides
    @Singleton
    fun provideExchangerDataSource(
        networkManager: INetworkManager,
        currencyRepository: ICurrencyLayerRepository,
        database: IExchangeDatabase
    ): IExchangeDataSource {
        val dao = database.exchangeRateDao()
        return ExchangeDataSource(networkManager, currencyRepository, dao)
    }

}