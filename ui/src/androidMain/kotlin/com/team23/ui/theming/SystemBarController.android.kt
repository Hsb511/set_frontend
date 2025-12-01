package com.team23.ui.theming

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat

@Composable
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
actual fun rememberSystemBarsController(): SystemBarsController {
    val activity = LocalActivity.current!!
    val window = activity.window

    return remember(window) {
        object : SystemBarsController {
            override fun setSystemBarsColor(
                statusBarColor: Color,
                navigationBarColor: Color,
                darkIcons: Boolean
            ) {
                window.statusBarColor = statusBarColor.toArgb()
                window.navigationBarColor = navigationBarColor.toArgb()

                WindowInsetsControllerCompat(window, window.decorView).apply {
                    isAppearanceLightStatusBars = darkIcons
                    isAppearanceLightNavigationBars = darkIcons
                }
            }
        }
    }
}
