package com.team23.ui.system

import android.content.ClipData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.toClipEntry

@RequiresApi(Build.VERSION_CODES.HONEYCOMB)
actual fun clipEntryOf(string: String): ClipEntry {
    return ClipData.newPlainText(string, string).toClipEntry()
}
