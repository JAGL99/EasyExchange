package com.jagl.exchangeapp.ui.screens.miss_token

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagl.core.preferences.SharedPrefManager
import com.jagl.data.datasource.currency.ICurrencyDataSource
import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency
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
        val result = currencyDataSource.getAvailableCurrencies()
            when(result){
                is ApiState.Error -> prefManager.clear()
                is ApiState.Success -> onSuccess()
            }
    }

}