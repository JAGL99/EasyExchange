package com.jagl.exchangeapp.app

import android.app.Application
import com.jagl.domain.crashlytics.CrashlyticsHelper
import com.jagl.exchangeapp.BuildConfig
import com.jagl.exchangeapp.analytics.FirebaseAnalyticsHelper
import dagger.hilt.android.HiltAndroidApp
import java.util.UUID

@HiltAndroidApp
class ExchangeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseAnalyticsHelper.init(this)
        CrashlyticsHelper.setUserId(UUID.randomUUID().toString())
        FirebaseAnalyticsHelper.logEvent(
            FirebaseAnalyticsHelper.Event.APP_OPEN,
            mapOf(
                FirebaseAnalyticsHelper.Param.APP_VERSION to BuildConfig.VERSION_NAME
            )
        )
    }
}