package com.jagl.exchangeapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.jagl.exchangeapp.analytics.FirebaseAnalyticsHelper
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        val app = super.newApplication(cl, HiltTestApplication::class.java.name, context)
        FirebaseAnalyticsHelper.init(app)
        return app
    }
}
