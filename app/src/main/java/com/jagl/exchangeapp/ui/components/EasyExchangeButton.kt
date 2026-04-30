package com.jagl.exchangeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jagl.exchangeapp.R

@Composable
fun EasyExchangeButton(
    textResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val brush =
        Brush.horizontalGradient(listOf(Color(0xFF0546ED), Color(0xFF849AFF)))

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier =
            Modifier
                .background(
                    brush = brush,
                    shape = ButtonDefaults.shape
                )
                .then(modifier),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(textResourceId),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun EasyExchangeButtonPreview() {
    EasyExchangeButton(
        textResourceId = R.string.procced,
        onClick = { /* Handle click */ },
    )
}