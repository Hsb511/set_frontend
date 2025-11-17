package com.team23.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team23.ui.debug.DebugManagementFAB
import com.team23.ui.debug.DebugViewModel
import com.team23.ui.navigation.NavigationHost
import com.team23.ui.snackbar.SetSnackbar
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    SetTheme {
        Box {
            NavigationHost()

            val debugViewModel = koinInject<DebugViewModel>()

            SetSnackbar(
                snackbarDataFlow = debugViewModel.snackbar,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            // TODO ONLY FOR DEV AND TEST
            DebugManagementFAB(
                onAction = debugViewModel::onAction,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
