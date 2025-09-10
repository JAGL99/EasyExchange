package com.jagl.exchangeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase de aplicación personalizada para inicializar componentes globales
 */
@HiltAndroidApp
class ExchangeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // La inicialización de la base de datos ahora se maneja con Hilt
    }

    companion object {
        // Constantes globales de la aplicación
        const val API_BASE_URL = "https://api.exchangerate-api.com/v4/"
    }
}