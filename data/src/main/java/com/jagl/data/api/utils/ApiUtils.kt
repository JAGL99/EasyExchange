package com.jagl.data.api.utils

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ApiUtils {

    /**
     * Obtiene un mensaje de error basado en la excepción
     * @param throwable Excepción que ocurrió
     * @return Mensaje de error amigable para el usuario
     */
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> "No se pudo conectar al servidor. Verifica tu conexión a Internet."
            is SocketTimeoutException -> "La conexión ha expirado. Inténtalo de nuevo más tarde."
            is HttpException -> "Error del servidor: ${throwable.code()}. Inténtalo de nuevo más tarde."
            is IOException -> "Error de red. Verifica tu conexión a Internet."
            else -> "Error desconocido: ${throwable.message}"
        }
    }
}