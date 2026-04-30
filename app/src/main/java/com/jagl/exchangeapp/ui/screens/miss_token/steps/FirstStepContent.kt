package com.jagl.exchangeapp.ui.screens.miss_token.steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jagl.exchangeapp.R
import com.jagl.exchangeapp.ui.components.EasyExchangeButton
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiEvent

@Composable
fun FirstStepContent(onEvent: (TokenUiEvent) -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BigLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                textAlign = TextAlign.Center,
                lineHeight = 56.sp,
                text = stringResource(R.string.wellcome_title),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.wellcome_message),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))



            EasyExchangeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                textResourceId = R.string.procced,
                onClick = { onEvent(TokenUiEvent.ShowNextStep) }
            )
        }
    }
}

@Composable
private fun BigLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val vectorPainter = ImageVector.vectorResource(id = R.drawable.bg_blops)
        Image(
            imageVector = vectorPainter,
            contentDescription = "Blured background",
            modifier = Modifier
                .blur(
                    radius = 30.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                ),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FirstStepContentPreview() {
    FirstStepContent(onEvent = {})
}