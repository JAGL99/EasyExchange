package com.jagl.core.di

import android.content.Context
import com.jagl.core.tropicalization.ITropicalization
import com.jagl.core.tropicalization.TropicalizationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TropicalizationModule {

    @Provides
    @Singleton
    fun provideTropicalization(@ApplicationContext context: Context): ITropicalization {
        return TropicalizationImpl(context)
    }
}