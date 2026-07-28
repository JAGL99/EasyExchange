package com.jagl.exchangeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenLoader(
    isVisible: Boolean,
    progressColor: Color = MaterialTheme.colorScheme.primary
) {
    if (isVisible) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(0.2f),
                color = progressColor,
                strokeWidth = 5.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullScreenLoaderPreview() {
    FullScreenLoader(isVisible = true)
}
