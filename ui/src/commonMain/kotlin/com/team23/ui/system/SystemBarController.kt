package com.team23.ui.system

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

interface SystemBarsController {

    fun setSystemBarsColor(
        statusBarColor: Color,
        navigationBarColor: Color,
        darkIcons: Boolean
    )
}

@Composable
expect fun rememberSystemBarsController(): SystemBarsController
