package com.jagl.exchangeapp.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

object FirebaseAnalyticsHelper {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    enum class Event(val eventName: String) {
        CURRENCY_CONVERSION("currency_conversion"),
        APP_OPEN("app_open"),
        DATA_LOAD("data_load"),
        ERROR_OCCURRED("error_occurred"),
        NAVIGATION("navigation"),
        USER_INTERACTION("user_interaction"),
        CURRENCY_SELECTION("currency_selection"),
        CONVERSION_RESULT("conversion_result"),
    }

    enum class Param(val paramName: String) {
        RESULT("success"),
        DATA_TYPE("data_type"),
        ERROR_MESSAGE("error_message"),
        APP_VERSION("app_version"),
        INTERACTION_TYPE("interaction_type"),
        NAVIGATION_DESTINATION("navigation_destination"),
    }

    fun logEvent(event: Event, params: Map<Param, Any>) {
        val bundle = android.os.Bundle()
        for ((key, value) in params) {
            try {
                when (value) {
                    is String -> bundle.putString(key.paramName, value)
                    is Boolean -> bundle.putBoolean(key.paramName, value)
                    else -> bundle.putString(key.paramName, value.toString())
                }
            }catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
        firebaseAnalytics.logEvent(event.eventName, bundle)
    }


}