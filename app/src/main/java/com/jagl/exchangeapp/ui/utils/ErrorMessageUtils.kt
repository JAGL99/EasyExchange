package com.jagl.exchangeapp.ui.utils

import com.jagl.data.api.utils.ApiUtils
import com.jagl.exchangeapp.R

object ErrorMessageUtils {

    fun getErrorMessage(error: String): Int {
        return when (error) {
            ApiUtils.REQUEST_ERROR -> R.string.error_request
            ApiUtils.CONNECTION_ERROR -> R.string.error_connection
            ApiUtils.NO_INTERNET_ERROR -> R.string.error_no_internet
            ApiUtils.INVALID_TOKEN_ERROR -> R.string.error_invalid_token
            ApiUtils.NO_RATE_ERROR -> R.string.error_no_rate
            ApiUtils.TIME_OUT_ERROR -> R.string.error_timeout
            else -> R.string.error_generic
        }
    }
}