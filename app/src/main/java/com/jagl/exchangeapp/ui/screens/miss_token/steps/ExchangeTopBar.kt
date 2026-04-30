package com.jagl.exchangeapp.ui.screens.miss_token.steps

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jagl.exchangeapp.R

@Composable
fun ExchangeTopBar(
    icon: ImageVector,
    textResourceId: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                modifier = Modifier
                    .size(32.dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                fontSize = 24.sp,
                text = stringResource(textResourceId),
                style = MaterialTheme.typography.headlineLarge,
                color = androidx.compose.ui.graphics.Color(Color.BLACK)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeTopBarPreview() {
    val appIcon = ImageVector.vectorResource(id = R.drawable.sparks)
    val textResourceId = R.string.app_name
    ExchangeTopBar(appIcon, textResourceId)
}