package com.jagl.exchangeapp.ui.screens.miss_token

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagl.core.preferences.SharedPrefManager
import com.jagl.data.datasource.currency.ICurrencyDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val currencyDataSource: ICurrencyDataSource,
    private val prefManager: SharedPrefManager
) : ViewModel() {


    fun evaluateToken(token: String, onSuccess: () -> Unit) = viewModelScope.launch {
        prefManager.saveString("TOKEN", token)
        val isSuccess = currencyDataSource.getAvailableCurrencies().isNotEmpty()
        if (!isSuccess) {
            prefManager.clear()
            println("Token inválido")
            return@launch
        }

        onSuccess()
    }

}