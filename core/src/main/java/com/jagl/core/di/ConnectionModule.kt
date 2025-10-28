package com.jagl.core.di

import android.content.Context
import com.jagl.core.network.NetworkManagerImpl
import com.jagl.core.network.INetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {

    @Provides
    @Singleton
    fun provideConnectionManager(
        @ApplicationContext context: Context
    ): INetworkManager {
        return NetworkManagerImpl(context)
    }
}