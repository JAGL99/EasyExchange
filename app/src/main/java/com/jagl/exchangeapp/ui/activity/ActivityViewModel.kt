package com.jagl.exchangeapp.ui.activity

import androidx.lifecycle.ViewModel
import com.jagl.core.network.INetworkManager
import com.jagl.core.preferences.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    networkManager: INetworkManager,
    private val prefManager: SharedPrefManager,
) : ViewModel() {
    fun getToken(): String {
        return prefManager.getString("TOKEN", "").orEmpty()
    }

    val internetConnection = networkManager.getInternetConnectionStatus()
}