package com.jagl.domain.model

sealed class ApiState<out T> {
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val throwable: Throwable? = null,val message: String) : ApiState<Nothing>()
}