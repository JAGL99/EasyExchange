package com.jagl.core.network

import android.Manifest
import javax.inject.Inject

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

class NetworkManagerImpl @Inject constructor(
    private val context: Context
) : INetworkManager {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun getInternetConnectionStatus(): Flow<NetworkStatus> = callbackFlow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available)
            }
            override fun onLost(network: Network) {
                trySend(NetworkStatus.Available)
            }
        }

        val networkRequest = android.net.NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val hasConnection = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        val status = when(hasConnection) {
            true -> NetworkStatus.Available
            false -> NetworkStatus.Available
        }
        trySend(status)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
