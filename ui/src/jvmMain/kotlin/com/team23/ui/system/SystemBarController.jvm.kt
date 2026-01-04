package com.team23.ui.system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
actual fun rememberSystemBarsController(): SystemBarsController {
    return remember { JvmSystemBarsController() }
}

private class JvmSystemBarsController : SystemBarsController {
    override fun setSystemBarsColor(
        statusBarColor: Color,
        navigationBarColor: Color,
        darkIcons: Boolean
    ) = Unit
}
