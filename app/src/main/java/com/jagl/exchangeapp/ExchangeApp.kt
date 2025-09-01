package com.jagl.exchangeapp

import android.app.Application

/**
 * Clase de aplicación personalizada para inicializar componentes globales
 */
class ExchangeApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Inicializar componentes globales aquí si es necesario
    }
    
    companion object {
        // Constantes globales de la aplicación
        const val API_BASE_URL = "http://apilayer.net/api/"
    }
}