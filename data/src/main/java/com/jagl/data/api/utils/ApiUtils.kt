package com.jagl.data.api.utils

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ApiUtils {

    const val GENERIC_ERROR = "Algo ah fallado. Inténtalo de nuevo más tarde."
    const val CONNECTION_ERROR = "No se pudo conectar al servidor. Verifica tu conexión a Internet."

    /**
     * Obtiene un mensaje de error basado en la excepción
     * @param throwable Excepción que ocurrió
     * @return Mensaje de error amigable para el usuario
     */
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> CONNECTION_ERROR
            is SocketTimeoutException -> "La conexión ha expirado. Inténtalo de nuevo más tarde."
            is HttpException -> "Error del servidor: ${throwable.code()}. Inténtalo de nuevo más tarde."
            is IOException -> "Error de red. Verifica tu conexión a Internet."
            else -> GENERIC_ERROR
        }
    }

    fun getErrorMessage(code: Int): String {
        return when (code) {
            101 -> "No se ha proporcionado una clave de acceso válida, favor de intentar con otra clave"
            else -> GENERIC_ERROR
        }
    }

    fun <T> safeCall(
        response: Response<T>,
        code: Int? = null,
        onMapResponse: ((T) -> T)? = null
    ): Result<T> {
        try {
            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(Exception(response.message()))
            }

            if (code != null) {
                return Result.failure(Exception(getErrorMessage(code)))
            }

            val body = response.body()!!

            return when (onMapResponse == null) {
                true -> Result.success(body)
                false -> Result.success(onMapResponse(body))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}