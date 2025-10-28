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
import com.jagl.exchangeapp.ui.screens.exchange.ExchangeScreen
import com.jagl.exchangeapp.ui.theme.ExchangeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val internetConnectionState =
                activityViewModel.internetConnection.collectAsState(initial = false)
            LaunchedEffect(internetConnectionState.value) {
                when (internetConnectionState.value) {
                    true -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Conexión a Internet disponible",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    false -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Sin conexión a Internet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            ExchangeAppTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExchangeScreen()
                }
            }
        }
    }
}