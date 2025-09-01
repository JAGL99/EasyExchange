package com.jagl.exchangeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jagl.exchangeapp.ui.components.AmountInput
import com.jagl.exchangeapp.ui.components.CurrencyDropdown
import com.jagl.exchangeapp.ui.viewmodel.ExchangeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    viewModel: ExchangeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Mostrar mensaje de error si existe
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(message = it)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conversor de Monedas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Moneda de origen
                Text(
                    text = "De:",
                    style = MaterialTheme.typography.titleMedium
                )
                CurrencyDropdown(
                    selectedCurrency = uiState.fromCurrency,
                    currencies = uiState.availableCurrencies,
                    onCurrencySelected = viewModel::updateFromCurrency
                )
                
                // Monto a convertir
                Spacer(modifier = Modifier.height(8.dp))
                AmountInput(
                    value = uiState.amount,
                    onValueChange = viewModel::updateAmount
                )
                
                // Botón para intercambiar monedas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilledIconButton(
                        onClick = { viewModel.swapCurrencies() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Intercambiar monedas"
                        )
                    }
                }
                
                // Moneda de destino
                Text(
                    text = "A:",
                    style = MaterialTheme.typography.titleMedium
                )
                CurrencyDropdown(
                    selectedCurrency = uiState.toCurrency,
                    currencies = uiState.availableCurrencies,
                    onCurrencySelected = viewModel::updateToCurrency
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Resultado de la conversión
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Resultado",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        } else if (uiState.convertedAmount.isNotEmpty()) {
                            Text(
                                text = uiState.convertedAmount,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Mostrar la tasa de cambio
                            uiState.exchangeRate?.let { rate ->
                                Text(
                                    text = "1 ${uiState.fromCurrency?.code} = ${String.format("%.4f", rate)} ${uiState.toCurrency?.code}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else if (uiState.amount.isEmpty()) {
                            Text(
                                text = "Ingrese un monto para convertir",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}