package com.jagl.exchangeapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Utilidades para verificar el estado de la red
 */
object NetworkUtils {

    /**
     * Verifica si el dispositivo tiene conexión a Internet
     * @param context Contexto de la aplicación
     * @return true si hay conexión a Internet, false en caso contrario
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    /**
     * Obtiene un mensaje de error basado en la excepción
     * @param throwable Excepción que ocurrió
     * @return Mensaje de error amigable para el usuario
     */
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is java.net.UnknownHostException -> "No se pudo conectar al servidor. Verifica tu conexión a Internet."
            is java.net.SocketTimeoutException -> "La conexión ha expirado. Inténtalo de nuevo más tarde."
            is retrofit2.HttpException -> "Error del servidor: ${throwable.code()}. Inténtalo de nuevo más tarde."
            is java.io.IOException -> "Error de red. Verifica tu conexión a Internet."
            else -> "Error desconocido: ${throwable.message}"
        }
    }
}