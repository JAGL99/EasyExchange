package com.jagl.domain.model

/**
 * Represents the state of an API call.
 * It can be either [Success] or [Error].
 */
sealed class ApiState<out T> {
    /**
     * Represents a successful API call.
     * @param data The data returned by the API.
     */
    data class Success<out T>(val data: T) : ApiState<T>()

    /**
     * Represents a failed API call.
     * @param message The error message.
     */
    data class Error(val message: String) : ApiState<Nothing>()
}