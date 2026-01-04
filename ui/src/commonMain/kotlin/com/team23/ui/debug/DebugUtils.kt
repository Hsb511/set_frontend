package com.team23.ui.debug

import com.team23.ui.BuildKonfig

fun isDebug(): Boolean = BuildKonfig.IS_DEBUG.toBoolean()
fun showSplashScreen(): Boolean = BuildKonfig.SHOW_CUSTOM_SPLASH_SCREEN.toBoolean()
