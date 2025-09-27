package com.jagl.exchangeapp.ui.screens.exchange

import com.jagl.domain.model.Currency

/**
 * Estado de la UI para la pantalla de conversión de monedas
 */
data class ExchangeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val availableCurrencies: List<Currency> = emptyList(),
    val fromCurrency: Currency? = null,
    val toCurrency: Currency? = null,
    val amount: String = "",
    val convertedAmount: String = "",
    val exchangeRate: Double? = null
)