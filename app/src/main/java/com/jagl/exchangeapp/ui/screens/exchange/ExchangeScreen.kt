package com.jagl.exchangeapp.ui.screens.exchange

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jagl.domain.model.Currency
import com.jagl.domain.model.ExchangeRate
import com.jagl.domain.model.getEquivalent
import com.jagl.exchangeapp.R
import com.jagl.exchangeapp.ui.components.AmountInput
import com.jagl.exchangeapp.ui.components.AnimatedAlert
import com.jagl.exchangeapp.ui.components.ExchangeResult
import com.jagl.exchangeapp.ui.components.SearchableCurrencyDropdown
import com.jagl.exchangeapp.ui.components.SwapButton
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                SwapButton(
                    modifier = Modifier.align(Alignment.End),
                    onSwap = { onEvent(ExchangeUiEvents.SwapCurrencies) }
                )

                // Moneda de origen
                Text(
                    text = stringResource(R.string.from),
                    style = MaterialTheme.typography.titleMedium
                )

                SearchableCurrencyDropdown(
                    currencySelected = uiState.value.fromCurrency,
                    avableCurrencies = uiState.value.availableCurrencies,
                    onCurrencySelected = { onEvent(ExchangeUiEvents.SelectFromCurrency(it)) }
                )

                // Monto a convertir
                Spacer(modifier = Modifier.height(8.dp))
                AmountInput(
                    value = uiState.value.amount,
                    onValueChange = { onEvent(ExchangeUiEvents.UpdateAmount(it)) }
                )

                // Moneda de destino
                Text(
                    text = stringResource(R.string.to),
                    style = MaterialTheme.typography.titleMedium
                )

                SearchableCurrencyDropdown(
                    currencySelected = uiState.value.toCurrency,
                    avableCurrencies = uiState.value.availableCurrencies,
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

                uiState.value.exchangeRate?.let { exchangeRate ->
                    ExchangeResult(
                        convertedAmount = uiState.value.convertedAmount,
                        exchangeRate = exchangeRate,
                        isLoading = uiState.value.isLoading
                    )
                }

            }

            uiState.value.errorMessage?.let { error ->
                AnimatedAlert(
                    message = error,
                    onDismiss = {
                        onEvent(ExchangeUiEvents.DismissError)
                    }
                )
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