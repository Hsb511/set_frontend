package com.team23.ui.debug

import com.team23.ui.BuildKonfig

fun isDebug(): Boolean = BuildKonfig.IS_DEBUG.toBoolean()
