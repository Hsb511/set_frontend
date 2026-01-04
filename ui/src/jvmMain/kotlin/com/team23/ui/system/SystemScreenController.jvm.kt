package com.team23.ui.system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberSystemScreenController(): SystemScreenController {
    return remember { JvmSystemScreenController() }
}

private class JvmSystemScreenController : SystemScreenController {
    override fun setKeepScreenOn() = Unit

    override fun clearKeepScreenOn() = Unit
}
