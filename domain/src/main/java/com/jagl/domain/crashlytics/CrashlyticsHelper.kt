package com.jagl.domain.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsHelper {
    private val firebaseCrashlytics: FirebaseCrashlytics by lazy {
        FirebaseCrashlytics.getInstance()
    }

    fun setUserId(userId: String) {
        firebaseCrashlytics.setUserId(userId)
    }

    fun logException(exception: Throwable) {
        firebaseCrashlytics.recordException(exception)
    }
}