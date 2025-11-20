package com.jagl.core.util

import androidx.compose.ui.focus.FocusRequester

object UiUtils {

    fun requestFocusByContent(
        content: String?,
        fR: FocusRequester
    ) {
        when (content.isNullOrBlank()) {
            false -> fR.captureFocus()
            true -> fR.freeFocus()
        }
    }
}