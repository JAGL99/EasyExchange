package com.jagl.exchangeapp.ui.screens.miss_token

sealed class TokenUiEvent {
    data object Idle : TokenUiEvent()
    data class UpdateToken(val token: String) : TokenUiEvent()
    data object CheckToken : TokenUiEvent()
    data object TokenIsValid : TokenUiEvent()
    data object OpenBrowser : TokenUiEvent()
    data object ShowNextStep : TokenUiEvent()
    data object ShowPreviousStep : TokenUiEvent()
    data class ShowError(val message: String) : TokenUiEvent()
    data object DismissError : TokenUiEvent()
}


