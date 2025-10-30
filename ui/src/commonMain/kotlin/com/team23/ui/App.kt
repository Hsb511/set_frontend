package com.team23.ui

import androidx.compose.runtime.Composable
import com.team23.ui.navigation.NavigationHost
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SetTheme {
        NavigationHost()
    }
}
