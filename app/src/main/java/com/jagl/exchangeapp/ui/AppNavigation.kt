package com.jagl.exchangeapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jagl.exchangeapp.analytics.FirebaseAnalyticsHelper
import com.jagl.exchangeapp.ui.screens.exchange.ExchangeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, "home") {
        composable("home") {
            FirebaseAnalyticsHelper.logEvent(
                FirebaseAnalyticsHelper.Event.NAVIGATION,
                mapOf(FirebaseAnalyticsHelper.Param.NAVIGATION_DESTINATION to "home")
            )
            ExchangeScreen()
        }
    }
}