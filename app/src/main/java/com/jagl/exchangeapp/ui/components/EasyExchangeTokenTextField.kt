package com.jagl.exchangeapp.ui.components

// removed unused painterResource import
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jagl.exchangeapp.R
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiEvent
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiState

@Composable
fun EasyExchangeTokenTextField(
    uiState: State<TokenUiState>,
    onEvent: (TokenUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val token = uiState.value.token
    val isError = uiState.value.errorMessage != null
    OutlinedTextField(
        value = token,
        onValueChange = {
            val isValidToken =
                it.matches(Regex("^[a-zA-Z0-9]{0,76}$")) // Only allow alphanumeric characters and limit length to 76
            if (isValidToken) {
                if (isError) onEvent(TokenUiEvent.DismissError)

                onEvent(TokenUiEvent.UpdateToken(it))
            }
        },
        placeholder = { Text(stringResource(R.string.enter_your_token)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        },
        supportingText = {
            uiState.value.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun EasyExchangeTokenTextFieldPreview() {
    val uiState = remember { mutableStateOf(TokenUiState()) }
    MaterialTheme {
        EasyExchangeTokenTextField(uiState = uiState, onEvent = {})
    }
}