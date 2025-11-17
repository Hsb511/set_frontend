package com.team23.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team23.ui.debug.DebugManagementFAB
import com.team23.ui.navigation.NavigationHost
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SetTheme {
        Box {
            NavigationHost()
            // TODO ONLY FOR DEV AND TEST
            DebugManagementFAB(
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
