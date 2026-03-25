package com.jagl.core.network

import kotlinx.coroutines.flow.Flow

interface INetworkManager {
    fun getInternetConnectionStatus(): Flow<NetworkStatus>

    fun isConnected(): Boolean
}