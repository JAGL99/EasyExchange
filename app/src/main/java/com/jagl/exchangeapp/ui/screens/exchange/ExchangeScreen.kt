package com.jagl.exchangeapp.ui.screens.exchange

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jagl.domain.model.Currency
import com.jagl.exchangeapp.ui.components.AmountInput
import com.jagl.exchangeapp.ui.components.SearchableCurrencyDropdown

@Composable
fun ExchangeScreen(
    viewModel: ExchangeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    ExchangeContent(
        uiState,
        snackbarHostState,
        viewModel::updateFromCurrency,
        viewModel::updateAmount,
        viewModel::updateToCurrency,
        viewModel::swapCurrencies,
        viewModel::performConversion
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExchangeContent(
    uiState: ExchangeUiState,
    snackbarHostState: SnackbarHostState,
    updateFromCurrency: (Currency) -> Unit,
    updateAmount: (String) -> Unit,
    updateToCurrency: (Currency) -> Unit,
    swapCurrencies: () -> Unit,
    performConversion: () -> Unit,
) {
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
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Moneda de origen
                Text(
                    text = "De:",
                    style = MaterialTheme.typography.titleMedium
                )
                SearchableCurrencyDropdown(
                    currencies = uiState.availableCurrencies,
                    onCurrencySelected = updateFromCurrency
                )

                // Monto a convertir
                Spacer(modifier = Modifier.height(8.dp))
                AmountInput(
                    value = uiState.amount,
                    onValueChange = updateAmount
                )

                // Botón para intercambiar monedas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilledIconButton(
                        onClick = { swapCurrencies() },
                        modifier = Modifier
                            .size(56.dp)
                            .padding(4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Intercambiar monedas",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Moneda de destino
                Text(
                    text = "A:",
                    style = MaterialTheme.typography.titleMedium
                )
                SearchableCurrencyDropdown(
                    currencies = uiState.availableCurrencies,
                    onCurrencySelected = updateToCurrency
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón para realizar la consulta
                FilledTonalButton(
                    onClick = { performConversion() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        "Calcular Conversión",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Resultado de la conversión
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedContent(
                            targetState = uiState.convertedAmount,
                            label = "result_animation"
                        ) { targetAmount ->
                            Text(
                                text = if (targetAmount.isNotEmpty()) "Resultado" else " ",
                                style = MaterialTheme.typography.headlineSmall,
                                color = animateColorAsState(
                                    if (targetAmount.isNotEmpty()) MaterialTheme.colorScheme.secondary
                                    else MaterialTheme.colorScheme.surface
                                ).value,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                strokeWidth = 4.dp
                            )
                            return@Column
                        }

                        if (uiState.convertedAmount.isNotEmpty()) {
                            Text(
                                text = uiState.convertedAmount,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Mostrar la tasa de cambio
                            uiState.exchangeRate?.let { rate ->
                                Text(
                                    text = "1 ${uiState.fromCurrency?.code} = ${
                                        String.format(
                                            "%.4f",
                                            rate
                                        )
                                    } ${uiState.toCurrency?.code}",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            return@Column
                        }

                        if (uiState.amount.isEmpty()) {
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

@Preview(showBackground = true)
@Composable
fun ExchangeScreenPreview() {
    ExchangeContent(
        uiState = ExchangeUiState(
            availableCurrencies = listOf(
                Currency("USD", "Dólar Estadounidense"),
                Currency("EUR", "Euro"),
                Currency("JPY", "Yen Japonés")
            ),
            fromCurrency = Currency("USD", "Dólar Estadounidense"),
            toCurrency = Currency("EUR", "Euro"),
            amount = "100",
            convertedAmount = "85.00",
            exchangeRate = 0.85,
            isLoading = false,
            errorMessage = null
        ),
        snackbarHostState = remember { SnackbarHostState() },
        updateFromCurrency = {},
        updateAmount = {},
        updateToCurrency = {},
        swapCurrencies = {},
        performConversion = {}
    )
}