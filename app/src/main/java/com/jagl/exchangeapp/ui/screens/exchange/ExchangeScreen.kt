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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jagl.core.extensions.EMPTY
import com.jagl.domain.model.Currency
import com.jagl.domain.model.ExchangeRate
import com.jagl.domain.model.getEquivalent
import com.jagl.exchangeapp.R
import com.jagl.exchangeapp.ui.components.AmountInput
import com.jagl.exchangeapp.ui.components.SearchableCurrencyDropdown
import java.util.Locale

@Composable
fun ExchangeScreen(
    viewModel: ExchangeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ExchangeContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExchangeContent(
    uiState: State<ExchangeUiState>,
    snackbarHostState: SnackbarHostState,
    onEvent: (ExchangeUiEvents) -> Unit
) {
    // Mostrar mensaje de error si existe
    LaunchedEffect(uiState.value.errorMessage) {
        uiState.value.errorMessage?.let {
            snackbarHostState.showSnackbar(message = it)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.currency_exchanges)) },
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
                    text = stringResource(R.string.from),
                    style = MaterialTheme.typography.titleMedium
                )
                SearchableCurrencyDropdown(
                    currencies = uiState.value.availableCurrencies,
                    onCurrencySelected = { onEvent(ExchangeUiEvents.SelectFromCurrency(it)) }
                )

                // Monto a convertir
                Spacer(modifier = Modifier.height(8.dp))
                AmountInput(
                    value = uiState.value.amount,
                    onValueChange = { onEvent(ExchangeUiEvents.UpdateAmount(it)) }
                )

                // Botón para intercambiar monedas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilledIconButton(
                        onClick = { onEvent(ExchangeUiEvents.SwapCurrencies) },
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
                    text = stringResource(R.string.to),
                    style = MaterialTheme.typography.titleMedium
                )
                SearchableCurrencyDropdown(
                    currencies = uiState.value.availableCurrencies,
                    onCurrencySelected = { onEvent(ExchangeUiEvents.SelectToCurrency(it)) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón para realizar la consulta
                FilledTonalButton(
                    onClick = { onEvent(ExchangeUiEvents.PerformConversion) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = stringResource(R.string.calculate_exchange),
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
                            targetState = uiState.value.convertedAmount,
                            label = "result_animation"
                        ) { targetAmount ->
                            Text(
                                text = if (targetAmount.isNotEmpty()) stringResource(R.string.result) else String.EMPTY,
                                style = MaterialTheme.typography.headlineSmall,
                                color = animateColorAsState(
                                    if (targetAmount.isNotEmpty()) MaterialTheme.colorScheme.secondary
                                    else MaterialTheme.colorScheme.surface
                                ).value,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.value.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                strokeWidth = 4.dp
                            )
                            return@Column
                        }

                        if (uiState.value.convertedAmount.isNotEmpty()) {
                            Text(
                                text = uiState.value.convertedAmount,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Mostrar la tasa de cambio
                            uiState.value.exchangeRate?.let { rate ->
                                Text(
                                    text = rate,
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            return@Column
                        }

                        if (uiState.value.amount.isEmpty()) {
                            Text(
                                text = stringResource(R.string.enter_amount_to_exchange),
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
    val uiState = remember {
        mutableStateOf(
            ExchangeUiState(
                isLoading = false,
                errorMessage = null,
                availableCurrencies = listOf(
                    Currency("USD", "Dólar estadounidense"),
                    Currency("EUR", "Euro"),
                    Currency("JPY", "Yen japonés"),
                    Currency("GBP", "Libra esterlina"),
                    Currency("AUD", "Dólar australiano"),
                    Currency("CAD", "Dólar canadiense"),
                    Currency("CHF", "Franco suizo"),
                    Currency("CNY", "Yuan chino"),
                    Currency("SEK", "Corona sueca"),
                    Currency("NZD", "Dólar neozelandés")
                ),
                fromCurrency = Currency("USD", "Dólar estadounidense"),
                toCurrency = Currency("EUR", "Euro"),
                amount = "100",
                convertedAmount = "$85.00",
                exchangeRate = ExchangeRate("USD", "EUR", 0.85).getEquivalent(Locale.getDefault())
            )
        )
    }
    ExchangeContent(
        uiState = uiState,
        snackbarHostState = remember { SnackbarHostState() },
        onEvent = {},
    )
}