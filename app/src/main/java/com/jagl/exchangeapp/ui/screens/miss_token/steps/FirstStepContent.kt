package com.jagl.exchangeapp.ui.screens.miss_token.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jagl.exchangeapp.R
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiEvent

@Composable
fun FirstStepContent(onEvent: (TokenUiEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.wellcome_title, stringResource(R.string.app_name)),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.wellcome_message),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onEvent(TokenUiEvent.ShowNextStep) }
        ) {
            Text(stringResource(R.string.procced))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstStepContentPreview() {
    FirstStepContent(onEvent = {})
}