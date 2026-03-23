package com.jagl.core.tropicalization

import android.content.Context
import java.util.Locale
import javax.inject.Inject

class TropicalizationImpl @Inject constructor(private val context: Context) : ITropicalization {
    companion object {
        val SUPPORTED_LANGUAGES = arrayOf("es-MX", "en-US")
    }

    private fun getDeviceLocale(context: Context): Locale {
        val userLocale = context.resources.configuration.locales.getFirstMatch(SUPPORTED_LANGUAGES)
        return userLocale ?: Locale(SUPPORTED_LANGUAGES.first())
    }

    override fun getLocale(): Locale = getDeviceLocale(context)
}