package com.jagl.exchangeapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jagl.core.extensions.EMPTY
import com.jagl.domain.model.ExchangeRate
import com.jagl.domain.model.getEquivalent
import com.jagl.exchangeapp.R
import java.util.Locale

@Composable
fun ExchangeResult(
    convertedAmount: String,
    exchangeRate: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 4.dp
                    )
                    return@Column
                }

                AnimatedContent(
                    targetState = convertedAmount,
                    label = "result_animation"
                ) { targetAmount ->
                    Text(
                        text = if (targetAmount.isNotEmpty()) stringResource(R.string.result) else String.EMPTY,
                        style = MaterialTheme.typography.headlineSmall,
                        color = animateColorAsState(
                            if (targetAmount.isNotEmpty()) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.surface
                        ).value
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (convertedAmount.isNotEmpty()) {
                    Text(
                        text = convertedAmount,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mostrar la tasa de cambio
                    exchangeRate?.let { rate ->
                        Text(
                            text = rate,
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val exchange = ExchangeRate("USD", "MXN", 12.0)
    val locale = Locale.US
    ExchangeResult(
        convertedAmount = "1",
        exchangeRate = exchange.getEquivalent(locale),
        isLoading = false
    )
}

