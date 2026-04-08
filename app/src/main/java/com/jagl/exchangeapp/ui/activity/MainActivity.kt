package com.jagl.exchangeapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.jagl.core.network.NetworkStatus
import com.jagl.exchangeapp.analytics.FirebaseAnalyticsHelper
import com.jagl.exchangeapp.ui.AppNavigation
import com.jagl.exchangeapp.ui.theme.ExchangeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val internetConnectionState = viewModel.internetConnection.collectAsState(initial = NetworkStatus.Idle)
            LaunchedEffect(internetConnectionState.value) {
                if (internetConnectionState.value == NetworkStatus.Unavailable) {
                    Toast.makeText(
                        this@MainActivity,
                        "No hay conexión a internet",
                        Toast.LENGTH_LONG
                    ).show()
                    FirebaseAnalyticsHelper.logEvent(
                        FirebaseAnalyticsHelper.Event.ERROR_OCCURRED,
                        mapOf(FirebaseAnalyticsHelper.Param.ERROR_MESSAGE to "No internet connection")
                    )
                }
            }

            ExchangeAppTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel.getToken())
                }
            }
        }
    }
}