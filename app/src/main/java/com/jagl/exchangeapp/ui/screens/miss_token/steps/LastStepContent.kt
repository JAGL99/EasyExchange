package com.jagl.exchangeapp.ui.screens.miss_token.steps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jagl.exchangeapp.R
import com.jagl.exchangeapp.ui.components.EasyExchangeButton
import com.jagl.exchangeapp.ui.components.EasyExchangeTokenTextField
import com.jagl.exchangeapp.ui.components.SpacerH16
import com.jagl.exchangeapp.ui.components.SpacerH4
import com.jagl.exchangeapp.ui.components.SpacerH8
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiEvent
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiState

@Composable
fun LastStepContent(
    uiState: State<TokenUiState>,
    onEvent: (TokenUiEvent) -> Unit
) {
    val context = LocalContext.current
    val instructionTexts = listOf(
        stringResource(R.string.token_message_instruction_1),
        stringResource(R.string.token_message_link),
        stringResource(R.string.token_message_instruction_2),
        stringResource(R.string.token_message_instruction_3),
        stringResource(R.string.token_message_instruction_4)
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                text = stringResource(R.string.token_header_instructions),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 24.sp
            )

            SpacerH8()

            instructionTexts.forEachIndexed { i,text ->
                if (i == 1) {
                    Text(
                        modifier = Modifier.clickable { onEvent(TokenUiEvent.OpenBrowser) },
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    SpacerH4()
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp
                    )
                }
            }
            SpacerH8()
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                text = stringResource(R.string.token_footer_instructions),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
            )

            SpacerH16()

            EasyExchangeTokenTextField(uiState, onEvent)

            SpacerH16()

            EasyExchangeButton(
                modifier = Modifier
                    .fillMaxWidth(),
                textResourceId = R.string.procced,
                onClick = {
                    if (uiState.value.token.isNotEmpty()) {
                        onEvent(TokenUiEvent.CheckToken)
                    } else {
                        onEvent(TokenUiEvent.ShowError(context.getString(R.string.no_empty_token)))
                    }
                }
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun LastStepContentPreview() {
    val uiState = remember { mutableStateOf(TokenUiState(step = 1)) }
    LastStepContent(uiState = uiState, onEvent = {})
}