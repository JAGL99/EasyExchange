package com.jagl.exchangeapp.ui.screens.miss_token

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagl.core.preferences.SharedPrefManager
import com.jagl.data.datasource.currency.ICurrencyDataSource
import com.jagl.domain.model.ApiState
import com.jagl.exchangeapp.analytics.FirebaseAnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val currencyDataSource: ICurrencyDataSource,
    private val prefManager: SharedPrefManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TokenUiState())
    val uiState: StateFlow<TokenUiState>
        get() = _uiState.asStateFlow()
    private val _uiEvent = MutableSharedFlow<TokenUiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val uiEvent: SharedFlow<TokenUiEvent>
        get() = _uiEvent.asSharedFlow()


    fun handleEvent(event: TokenUiEvent) {
        when (event) {
            TokenUiEvent.CheckToken -> {
                FirebaseAnalyticsHelper.logEvent(
                    FirebaseAnalyticsHelper.Event.USER_INTERACTION,
                    mapOf(FirebaseAnalyticsHelper.Param.INTERACTION_TYPE to "check_token")
                )
                evaluateToken(uiState.value)
            }

            TokenUiEvent.DismissError -> dismissError()
            is TokenUiEvent.ShowError -> showError(event.message)
            is TokenUiEvent.UpdateToken -> updateToken(event.token)
            is TokenUiEvent.OpenBrowser -> {
                FirebaseAnalyticsHelper.logEvent(
                    FirebaseAnalyticsHelper.Event.NAVIGATION,
                    mapOf(FirebaseAnalyticsHelper.Param.NAVIGATION_DESTINATION to "browser")
                )
                openBrowser()
            }

            is TokenUiEvent.ShowPreviousStep -> {
                FirebaseAnalyticsHelper.logEvent(
                    FirebaseAnalyticsHelper.Event.NAVIGATION,
                    mapOf(FirebaseAnalyticsHelper.Param.NAVIGATION_DESTINATION to "previous_step")
                )
                updateStep(-1)
            }

            is TokenUiEvent.ShowNextStep -> {
                FirebaseAnalyticsHelper.logEvent(
                    FirebaseAnalyticsHelper.Event.NAVIGATION,
                    mapOf(FirebaseAnalyticsHelper.Param.NAVIGATION_DESTINATION to "next_step")
                )
                updateStep(1)
            }

            else -> Unit
        }
    }

    private fun updateStep(newStep: Int) {
        _uiState.update { currentState ->
            currentState.copy(step = currentState.step + newStep)
        }
    }

    private fun openBrowser() = viewModelScope.launch {
        _uiEvent.emit(TokenUiEvent.OpenBrowser)
    }


    private fun dismissError() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }

    private fun updateToken(token: String) {
        _uiState.update { currentState ->
            currentState.copy(token = token)
        }
    }

    private fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    private fun evaluateToken(state: TokenUiState) = viewModelScope.launch {
        prefManager.saveString("TOKEN", state.token)
        val result = currencyDataSource.getAvailableCurrencies()
        when (result) {
            is ApiState.Error -> {
                prefManager.clear()
                showError(result.message)
            }

            is ApiState.Success -> _uiEvent.emit(TokenUiEvent.TokenIsValid)
        }
    }

}