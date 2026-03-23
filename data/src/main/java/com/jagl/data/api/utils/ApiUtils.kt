package com.jagl.data.api.utils

import com.jagl.data.api.model.CurrencyLayerResponse
import com.jagl.domain.model.ApiState
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ApiUtils {

    const val GENERIC_ERROR = "Ups algo fallo. Inténtalo de nuevo más tarde."
    const val REQUEST_ERROR = "Hubo un problema con la solicitud. Verifica tu conexión o datos."
    const val CONNECTION_ERROR = "No se pudo conectar al servidor. Verifica tu conexión a Internet."
    const val NO_INTERNET_ERROR =
        "No se tiene conexión a internet, conectate a una red y vuelvelo a intentar"
    const val INVALID_TOKEN_ERROR =
        "No se ha proporcionado una clave de acceso válida, favor de intentar con otra clave"

    const val NO_RATE_ERROR = "No se encontro un tipo de cambio valido para estas divisas, favor de intentar otra opción"
    const val TIME_OUT_ERROR = "La conexión ha expirado. Inténtalo de nuevo más tarde."

    private fun getCurrencyLayerCodeMessage(code: Int): String {
        return when (code) {
            101 -> INVALID_TOKEN_ERROR
            else -> GENERIC_ERROR
        }
    }


    private fun getErrorMessage(throwable: Throwable?): String {
        return when {
            throwable is UnknownHostException ||
                    throwable is IOException ||
                    throwable is HttpException -> CONNECTION_ERROR

            throwable is SocketTimeoutException -> TIME_OUT_ERROR
            else -> GENERIC_ERROR
        }
    }


    private fun getHttpMessage(code: Int): String {
        return when (code) {
            in 400..499 -> REQUEST_ERROR
            in 500..599 -> CONNECTION_ERROR
            else -> GENERIC_ERROR
        }
    }


    suspend fun <T> safeResultCall(request: suspend () -> Result<T>): Result<T> = try {
        request()
    } catch (e: Exception) {
        Result.failure(Exception(getErrorMessage(e.cause)))
    }

    suspend fun <T> safeApiStateCall(request: suspend () -> ApiState<T>): ApiState<T> = try {
        request()
    } catch (e: Exception) {
        ApiState.Error(getErrorMessage(e.cause))
    }

    fun <T : CurrencyLayerResponse> safeMap(
        response: Response<T>,
        onMapResponse: ((T) -> T)? = null
    ): Result<T> {
        try {
            if (!response.isSuccessful || response.body() == null)
                return Result.failure(Exception(getHttpMessage(response.code())))

            val body = response.body()!!

            if (!body.success) {
                val message = body.error?.code?.let { code ->
                    getCurrencyLayerCodeMessage(code)
                } ?: GENERIC_ERROR
                return Result.failure(Exception(message))
            }

            return Result.success(onMapResponse?.invoke(body) ?: body)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}