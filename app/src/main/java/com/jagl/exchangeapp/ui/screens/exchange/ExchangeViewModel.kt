package com.jagl.exchangeapp.ui.screens.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagl.core.tropicalization.ITropicalization
import com.jagl.core.util.DateUtils
import com.jagl.data.datasource.currency.ICurrencyDataSource
import com.jagl.data.datasource.exchangeRate.IExchangeDataSource
import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency
import com.jagl.domain.model.ExchangeRate
import com.jagl.domain.model.getEquivalent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Date
import javax.inject.Inject
import java.util.Currency as CurrencyExchange

/**
 * ViewModel para la pantalla de conversión de monedas
 */
@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val exchangeDataSource: IExchangeDataSource,
    private val currencyDataSource: ICurrencyDataSource,
    private val tropicalization: ITropicalization,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeUiState())
    val uiState: StateFlow<ExchangeUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    /**
     * Carga la lista de monedas disponibles
     */
    private fun loadCurrencies() = viewModelScope.launch {
        val result: ApiState<List<Currency>> = currencyDataSource.getAvailableCurrencies()
        when (result) {
            is ApiState.Error -> println(result.message)
            is ApiState.Success -> {
                val currencies = result.data
                _uiState.update { currentState ->
                    currentState.copy(availableCurrencies = currencies)
                }
            }
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
        convertAmount()
    }

    fun handleEvent(event: ExchangeUiEvents) {
        when (event) {
            ExchangeUiEvents.PerformConversion -> convertAmount()
            ExchangeUiEvents.SwapCurrencies -> swapCurrencies()
            is ExchangeUiEvents.UpdateAmount -> updateAmount(event.amount)
            is ExchangeUiEvents.SelectFromCurrency -> updateFromCurrency(event.fromCurrency)
            is ExchangeUiEvents.SelectToCurrency -> updateToCurrency(event.toCurrency)
            ExchangeUiEvents.Idle -> return
            ExchangeUiEvents.DismissError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    /**
     * Converts the amount from the source currency to the target currency
     */
    private fun convertAmount() {
        val currentState = _uiState.value
        val amount = currentState.amount.toDoubleOrNull() ?: 0.0
        val availableCurrencies = currentState.availableCurrencies
        val fromCurrency = currentState.fromCurrency
        val toCurrency = currentState.toCurrency
        val locale = tropicalization.getLocale()
        val date = DateUtils.getDateWithFormat(
            locale = locale,
            date = Date()
        )

        if (amount <= 0) {
            _uiState.update { it.copy(convertedAmount = "") }
            return
        }

        if (evaluateCurrency(fromCurrency, availableCurrencies)) {
            _uiState.update { it.copy(errorMessage = "Seleccione una divisa inicial valida") }
            return
        }

        if (evaluateCurrency(toCurrency, availableCurrencies)) {
            _uiState.update { it.copy(errorMessage = "Seleccione una divisa destino valida") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val state: ApiState<ExchangeRate> = exchangeDataSource.getExchangeRate(
                    amount,
                    date,
                    fromCurrency!!,
                    toCurrency!!
                )
                when (state) {
                    is ApiState.Error -> {
                        state.throwable?.printStackTrace()
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = state.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }

                    is ApiState.Success -> {
                        val exchangeRate = state.data
                        val formatter = NumberFormat.getCurrencyInstance(locale).apply {
                            currency = CurrencyExchange.getInstance(toCurrency.code)
                        }
                        _uiState.update { currentState ->
                            currentState.copy(
                                convertedAmount = formatter.format(exchangeRate.rate),
                                exchangeRate = exchangeRate.getEquivalent(locale),
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = e.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun evaluateCurrency(
        currency: Currency?,
        availableCurrencies: List<Currency>
    ): Boolean {
        var isInvalid = false

        if (currency == null) isInvalid = true
        if (availableCurrencies.contains(currency).not()) isInvalid = true

        return isInvalid
    }
}