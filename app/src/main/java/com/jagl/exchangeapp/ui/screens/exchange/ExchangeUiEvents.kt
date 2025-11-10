package com.jagl.exchangeapp.ui.screens.exchange

import com.jagl.domain.model.Currency

/**
 * Eventos de la UI para la pantalla de conversión de monedas
 */
sealed class ExchangeUiEvents {
    object Idle : ExchangeUiEvents()
    data class UpdateAmount(val amount: String) : ExchangeUiEvents()
    data class SelectFromCurrency(val fromCurrency: Currency) : ExchangeUiEvents()
    data class SelectToCurrency(val toCurrency: Currency) : ExchangeUiEvents()
    object SwapCurrencies : ExchangeUiEvents()
    object PerformConversion : ExchangeUiEvents()
}