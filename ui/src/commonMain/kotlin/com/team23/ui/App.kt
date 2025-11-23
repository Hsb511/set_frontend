package com.team23.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.team23.ui.debug.DebugManagementFAB
import com.team23.ui.debug.DebugUiEvent
import com.team23.ui.debug.DebugViewModel
import com.team23.ui.debug.isDebug
import com.team23.ui.navigation.NavigationHost
import com.team23.ui.snackbar.SetSnackbar
import com.team23.ui.theming.SetTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    SetTheme {
        Box {
            NavigationHost()

            if (isDebug()) {
                val lifecycleOwner = LocalLifecycleOwner.current
                val debugViewModel = koinInject<DebugViewModel>()

                SetSnackbar(
                    snackbarDataFlow = debugViewModel.snackbar,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )

                val isDebugExpanded = remember { mutableStateOf(false) }
                var isLoading by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        withContext(Dispatchers.Main.immediate) {
                            debugViewModel.uiEvent.collect { uiEvent ->
                                when(uiEvent) {
                                    is DebugUiEvent.LoadingStarted -> {
                                        isLoading = true
                                    }
                                    is DebugUiEvent.LoadingFinished -> {
                                        isLoading = false
                                        isDebugExpanded.value = !uiEvent.isSuccess
                                    }
                                }
                            }
                        }
                    }
                }

                DebugManagementFAB(
                    isExpanded = isDebugExpanded,
                    isLoading = isLoading,
                    onAction = debugViewModel::onAction,
                    modifier = Modifier
                        .safeDrawingPadding()
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}
