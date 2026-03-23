package com.jagl.core.network

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
    object Idle : NetworkStatus()
}