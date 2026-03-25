package com.jagl.exchangeapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jagl.exchangeapp.R

@Composable
fun SwapButton(
    onSwap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text = stringResource(R.string.swap_button)
    Box(modifier = modifier) {
        FilledIconButton(
            onClick = onSwap,
            modifier = Modifier
                .size(48.dp)
                .testTag(text)
                .semantics { contentDescription = text },
            shape = MaterialTheme.shapes.small,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_swap),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSwapButton() {
    SwapButton(onSwap = { println("SWAP") })
}