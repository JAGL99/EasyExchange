package com.jagl.exchangeapp.ui.screens.miss_token.steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.jagl.exchangeapp.ui.components.SpacerH16
import com.jagl.exchangeapp.ui.components.SpacerH32
import com.jagl.exchangeapp.ui.components.SpacerH8
import com.jagl.exchangeapp.ui.screens.miss_token.TokenUiEvent

@Composable
fun FirstStepContent(onEvent: (TokenUiEvent) -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            BigLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )

            SpacerH16()

            Text(
                textAlign = TextAlign.Center,
                lineHeight = 56.sp,
                text = stringResource(R.string.wellcome_title),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 48.sp
            )

            SpacerH16()

            Text(
                textAlign = TextAlign.Start,
                text = stringResource(R.string.wellcome_message),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp
            )

            SpacerH32()

            EasyExchangeButton(
                modifier = Modifier.fillMaxWidth(),
                textResourceId = R.string.procced,
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                onClick = { onEvent(TokenUiEvent.ShowNextStep) }
            )

            SpacerH16()
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