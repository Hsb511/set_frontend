package com.team23.ui.debug

import com.team23.ui.BuildKonfig


actual fun isDebug(): Boolean {
    return BuildKonfig.IS_DEBUG.toBoolean()
}
