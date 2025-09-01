package com.jagl.exchangeapp.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Clase que proporciona las dependencias de red para la aplicación
 */
object NetworkModule {
    
    /**
     * Crea una instancia de OkHttpClient con logging para depuración
     */
    private fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Crea una instancia de Moshi para la serialización/deserialización JSON
     */
    private fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    
    /**
     * Crea una instancia de Retrofit para realizar llamadas a la API
     */
    private fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(com.jagl.exchangeapp.ExchangeApp.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    
    /**
     * Proporciona una instancia de la API de tasas de cambio
     */
    val exchangeRateApi: ExchangeRateApi by lazy {
        val okHttpClient = provideOkHttpClient()
        val moshi = provideMoshi()
        val retrofit = provideRetrofit(okHttpClient, moshi)
        retrofit.create(ExchangeRateApi::class.java)
    }
}