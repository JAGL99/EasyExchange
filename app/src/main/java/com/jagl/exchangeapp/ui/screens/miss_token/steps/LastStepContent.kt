package com.jagl.exchangeapp.ui.screens.miss_token.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jagl.exchangeapp.R
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiEvent
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiState

@Composable
fun LastStepContent(
    uiState: State<TokenUiState>,
    onEvent: (TokenUiEvent) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = stringResource(R.string.token_instructions),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))


        TextField(
            value = uiState.value.token,
            onValueChange = {
                val isValidToken =
                    it.matches(Regex("^[a-zA-Z0-9]{0,76}$")) // Only allow alphanumeric characters and limit length to 76
                if (isValidToken) {
                    onEvent(TokenUiEvent.UpdateToken(it))
                }
            },
            label = { Text(stringResource(R.string.enter_your_token)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (uiState.value.token.isNotEmpty()) {
                    onEvent(TokenUiEvent.CheckToken)
                } else {
                    onEvent(TokenUiEvent.ShowError(context.getString(R.string.no_empty_token)))
                }
            }
        ) {
            Text(stringResource(R.string.procced))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEvent(TokenUiEvent.OpenBrowser) }
        ) {
            Text(stringResource(R.string.get_token))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LastStepContentPreview() {
    val uiState = remember { mutableStateOf(TokenUiState(step = 1)) }
    LastStepContent(uiState = uiState, onEvent = {})
}