package com.jagl.core.network

import kotlinx.coroutines.flow.Flow

fun interface INetworkManager {
    fun hasInternetConnection(): Flow<Boolean>
}