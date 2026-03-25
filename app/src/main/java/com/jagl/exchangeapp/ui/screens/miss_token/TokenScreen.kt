package com.jagl.exchangeapp.ui.screens.miss_token

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jagl.exchangeapp.ui.components.AnimatedAlert
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TokenScreen(
    navigateToHome: () -> Unit,
    viewModel: TokenViewModel = hiltViewModel()
) {
    val url =
        "https://manage.exchangeratesapi.io/login?u=https%3A%2F%2Fmanage.exchangeratesapi.io%2Fdashboard"
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                TokenUiEvent.TokenIsValid -> navigateToHome()
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        uiState.value.errorMessage?.let { error ->
            AnimatedAlert(
                message = error,
                onDismiss = {
                    viewModel.handleEvent(TokenUiEvent.DismissError)
                }
            )
        }

        Text(text = "Please enter your token:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.value.token,
            onValueChange = {
                val isValidToken =
                    it.matches(Regex("^[a-zA-Z0-9]{0,76}$")) // Only allow alphanumeric characters and limit length to 76
                if (isValidToken) {
                    viewModel.handleEvent(TokenUiEvent.UpdateToken(it))
                }
            },
            label = { Text("Token") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (uiState.value.token.isNotEmpty()) {
                    viewModel.handleEvent(TokenUiEvent.CheckToken)
                } else {
                    viewModel.handleEvent(TokenUiEvent.ShowError("Require token"))
                }
            }
        ) {
            Text("Check token")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        ) {
            Text("Open Browser")
        }
    }
}
