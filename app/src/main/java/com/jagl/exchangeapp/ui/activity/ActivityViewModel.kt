package com.jagl.exchangeapp.ui.activity

import androidx.lifecycle.ViewModel
import com.jagl.core.network.INetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val networkManager: INetworkManager
) : ViewModel() {

    val internetConnection = networkManager.getInternetConnectionStatus()
}