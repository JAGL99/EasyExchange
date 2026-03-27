package com.jagl.exchangeapp.ui.screens.miss_token

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.jagl.exchangeapp.ui.components.AnimatedAlert
import com.jagl.exchangeapp.ui.screens.miss_token.steps.FirstStepContent
import com.jagl.exchangeapp.ui.screens.miss_token.steps.LastStepContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TokenScreen(
    navigateToHome: () -> Unit,
    viewModel: TokenViewModel = hiltViewModel()
) {
    val url =
        "https://manage.exchangeratesapi.io/login?u=https%3A%2F%2Fmanage.exchangeratesapi.io%2Fdashboard"
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                TokenUiEvent.TokenIsValid -> navigateToHome()
                TokenUiEvent.OpenBrowser -> {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                }

                else -> Unit
            }
        }
    }
    BackHandler {
        if (uiState.value.step > 0) {
            viewModel.handleEvent(TokenUiEvent.ShowPreviousStep)
            return@BackHandler
        }
    }

    TokenScreenContent(uiState, viewModel::handleEvent)
}

@Composable
private fun TokenScreenContent(
    uiState: State<TokenUiState>,
    onEvent: (TokenUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        uiState.value.errorMessage?.let { error ->
            AnimatedAlert(
                message = error,
                onDismiss = {
                    onEvent(TokenUiEvent.DismissError)
                }
            )
        }

        AnimatedContent(
            targetState = uiState.value.step,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut()
                    )
                } else {
                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> width } + fadeOut()
                    )
                }.using(SizeTransform(clip = false))
            },
            label = "StepTransition"
        ) { targetStep ->
            when (targetStep) {
                0 -> FirstStepContent(onEvent)
                1 -> LastStepContent(uiState, onEvent)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TokenScreenPreview() {
    val uiState = remember { mutableStateOf(TokenUiState()) }
    TokenScreenContent(
        uiState = uiState,
        onEvent = {}
    )
}
