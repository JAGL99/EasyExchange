package com.jagl.exchangeapp.ui.screens.miss_token

data class TokenUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val token: String = "",
    val step: Int = 0
)