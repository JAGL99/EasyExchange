package com.jagl.data.api.utils

import com.jagl.data.api.model.ErrorDto
import com.jagl.domain.model.ApiState
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ApiUtils {

    const val GENERIC_ERROR = "Oops, something went wrong. Please try again later."
    const val REQUEST_ERROR =
        "There was a problem with the request. Please check your connection or data."
    const val CONNECTION_ERROR =
        "Could not connect to the server. Please check your internet connection."
    const val NO_INTERNET_ERROR =
        "No internet connection, please connect to a network and try again"
    const val INVALID_TOKEN_ERROR =
        "A valid access key has not been provided, please try another key"

    const val NO_RATE_ERROR =
        "No valid exchange rate was found for these currencies, please try another option"
    const val TIME_OUT_ERROR = "The connection has expired. Please try again later."

    private fun getFrankfurterCodeMessage(code: Int): String {
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
        println("safeResultCall")
        request()
    } catch (e: Exception) {
        e.printStackTrace()
        println("safeResultCall exception: ${e.cause}")
        println("safeResultCall error: ${e.message}")
        Result.failure(Exception(getErrorMessage(e.cause)))
    }

    suspend fun <T> safeApiStateCall(request: suspend () -> ApiState<T>): ApiState<T> = try {
        println("safeApiStateCall")
        request()
    } catch (e: Exception) {
        e.printStackTrace()
        println("safeApiStateCall exception: ${e.cause}")
        println("safeApiStateCall error: ${e.message}")
        ApiState.Error(getErrorMessage(e.cause))
    }

    fun <T : List<*>> safeMap(
        response: Response<T?>,
        onMapResponse: ((T) -> T)? = null
    ): Result<T> {
        println("safeMap called with response: $response")
        try {
            println("safeMap: isSuccessful=${response.isSuccessful}, body=${response.body()}")
            if (!response.isSuccessful || response.body() == null)
                return Result.failure(Exception(getHttpMessage(response.code())))

            println("safeMap: response body is not null, proceeding to map")
            val bodyList = response.body()!!

            println("safeMap: bodyList size=${bodyList.size}")
            if (bodyList.isEmpty()) {
                println("safeMap: bodyList is empty, attempting to parse error response")
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(ErrorDto::class.java)
                val error = jsonAdapter.fromJson(bodyList.toString())
                val message =
                    error?.status?.let { code -> getFrankfurterCodeMessage(code) } ?: GENERIC_ERROR
                return Result.failure(Exception(message))
            }

            println("safeMap: Mapping bodyList to desired format")
            return Result.success(onMapResponse?.invoke(bodyList) ?: bodyList)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}