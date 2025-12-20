package com.team23.ui.system

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberSystemScreenController(): SystemScreenController {
    val window = LocalActivity.current?.window

    return remember(window) {
        object : SystemScreenController {
            override fun setKeepScreenOn() {
                window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }

            override fun clearKeepScreenOn() {
                window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}
