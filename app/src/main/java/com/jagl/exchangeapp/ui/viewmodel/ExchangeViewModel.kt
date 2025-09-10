package com.jagl.exchangeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagl.exchangeapp.data.model.Currency
import com.jagl.exchangeapp.data.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

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

/**
 * ViewModel para la pantalla de conversión de monedas
 */
@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val repository: ExchangeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeUiState())
    val uiState: StateFlow<ExchangeUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    /**
     * Carga la lista de monedas disponibles
     */
    private fun loadCurrencies() {
        val currencies = repository.getAvailableCurrencies()
        _uiState.update { currentState ->
            currentState.copy(
                availableCurrencies = currencies,
                fromCurrency = currencies.find { it.code == "USD" },
                toCurrency = currencies.find { it.code == "EUR" }
            )
        }
    }

    /**
     * Actualiza la moneda de origen
     */
    fun updateFromCurrency(currency: Currency) {
        _uiState.update { currentState ->
            currentState.copy(fromCurrency = currency)
        }
    }

    /**
     * Actualiza la moneda de destino
     */
    fun updateToCurrency(currency: Currency) {
        _uiState.update { currentState ->
            currentState.copy(toCurrency = currency)
        }
    }

    /**
     * Actualiza el monto a convertir
     */
    fun updateAmount(amount: String) {
        _uiState.update { currentState ->
            currentState.copy(amount = amount)
        }
    }

    /**
     * Intercambia las monedas de origen y destino
     */
    fun swapCurrencies() {
        _uiState.update { currentState ->
            currentState.copy(
                fromCurrency = currentState.toCurrency,
                toCurrency = currentState.fromCurrency
            )
        }
    }
    
    /**
     * Método público para iniciar la conversión de moneda manualmente
     */
    fun performConversion() {
        convertAmount()
    }

    /**
     * Convierte el monto de la moneda de origen a la moneda de destino
     */
    private fun convertAmount() {
        val currentState = _uiState.value
        val amount = currentState.amount.toDoubleOrNull() ?: 0.0
        val fromCurrency = currentState.fromCurrency?.code ?: return
        val toCurrency = currentState.toCurrency?.code ?: return

        if (amount <= 0) {
            _uiState.update { it.copy(convertedAmount = "") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                repository.convertCurrency(amount, fromCurrency, toCurrency)
                    .onSuccess { convertedAmount ->
                        val formatter =
                            NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
                                currency = java.util.Currency.getInstance(toCurrency)
                            }

                        _uiState.update { currentState ->
                            currentState.copy(
                                convertedAmount = formatter.format(convertedAmount),
                                exchangeRate = convertedAmount / amount,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure { error ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = error.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = e.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
            }
        }
    }
}