package com.jagl.exchangeapp.ui.screens.exchange

import com.jagl.domain.model.Currency

/**
 * Eventos de la UI para la pantalla de conversión de monedas
 */
sealed class ExchangeUiEvents {
    data object Idle : ExchangeUiEvents()
    data class UpdateAmount(val amount: String) : ExchangeUiEvents()
    data class SelectFromCurrency(val fromCurrency: Currency) : ExchangeUiEvents()
    data class SelectToCurrency(val toCurrency: Currency) : ExchangeUiEvents()
    data object SwapCurrencies : ExchangeUiEvents()
    data object PerformConversion : ExchangeUiEvents()
    data object DismissError: ExchangeUiEvents()
    data object ShowExitDialog: ExchangeUiEvents()
    data object DismissExitDialog: ExchangeUiEvents()
}