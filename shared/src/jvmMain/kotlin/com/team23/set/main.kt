package com.team23.set

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.team23.ui.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
