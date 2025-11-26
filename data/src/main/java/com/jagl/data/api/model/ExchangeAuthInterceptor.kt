package com.jagl.data.api.model

import com.jagl.core.preferences.SharedPrefManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class ExchangeAuthInterceptor(
    private val sharedPreferences: SharedPrefManager
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val accessKey  = sharedPreferences.getString("TOKEN", null).orEmpty()
        accessKey.ifBlank {
            throw AccessKeyException("Access key is missing or empty")
        }


        val newUrl: HttpUrl = originalUrl.newBuilder()
            .addQueryParameter("access_key", accessKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

}


