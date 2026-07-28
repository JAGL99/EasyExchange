package com.jagl.data.api.utils

import com.jagl.data.api.model.CurrencyLayerResponse
import com.jagl.domain.model.ApiState
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ApiUtils {

    const val GENERIC_ERROR = "Oops, something went wrong. Please try again later."
    const val REQUEST_ERROR = "There was a problem with the request. Please check your connection or data."
    const val CONNECTION_ERROR = "Could not connect to the server. Please check your internet connection."
    const val NO_INTERNET_ERROR =
        "No internet connection, please connect to a network and try again"
    const val INVALID_TOKEN_ERROR =
        "A valid access key has not been provided, please try another key"

    const val NO_RATE_ERROR = "No valid exchange rate was found for these currencies, please try another option"
    const val TIME_OUT_ERROR = "The connection has expired. Please try again later."

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