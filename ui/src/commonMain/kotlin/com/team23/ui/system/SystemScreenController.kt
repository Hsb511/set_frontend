package com.team23.ui.system

import androidx.compose.runtime.Composable

interface SystemScreenController {

    fun setKeepScreenOn()

    fun clearKeepScreenOn()
}

@Composable
expect fun rememberSystemScreenController(): SystemScreenController
