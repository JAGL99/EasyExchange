package com.jagl.exchangeapp.ui.screens.miss_token

sealed class TokenUiEvent {
    object Idle : TokenUiEvent()
    data class UpdateToken(val token: String) : TokenUiEvent()
    object CheckToken : TokenUiEvent()
    object TokenIsValid : TokenUiEvent()
    data class ShowError(val message: String): TokenUiEvent()
    object DismissError : TokenUiEvent()
}


