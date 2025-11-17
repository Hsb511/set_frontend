package com.team23.ui.debug

import com.team23.ui.BuildConfig

actual fun isDebug(): Boolean {
    return BuildConfig.DEBUG
}
