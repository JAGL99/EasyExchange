package com.jagl.exchangeapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jagl.exchangeapp.analytics.FirebaseAnalyticsHelper
import com.jagl.exchangeapp.ui.screens.exchange.ExchangeScreen
import com.jagl.exchangeapp.ui.screens.miss_token.TokenScreen

@Composable
fun AppNavigation(token: String) {
    val navController = rememberNavController()
    val noHasToken = token.isEmpty()
    val startDestination = if (noHasToken) "token" else "home"
    NavHost(navController, startDestination) {
        composable("home") {
            FirebaseAnalyticsHelper.logEvent(
                FirebaseAnalyticsHelper.Event.NAVIGATION,
                mapOf(FirebaseAnalyticsHelper.Param.NAVIGATION_DESTINATION to "home")
            )
            ExchangeScreen()
        }
        composable("token") {
            FirebaseAnalyticsHelper.logEvent(
                FirebaseAnalyticsHelper.Event.NAVIGATION,
                mapOf(FirebaseAnalyticsHelper.Param.NAVIGATION_DESTINATION to "token")
            )
            TokenScreen(
                navigateToHome = {
                    navController.navigate("home") {
                        popUpTo("token") { inclusive = true }
                    }
                }
            )
        }
    }
}