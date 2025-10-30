package com.team23.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team23.ui.game.Game
import com.team23.ui.game.GameViewModel
import com.team23.ui.snackbar.SetSnackbar
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    SetTheme {
        val gameVM = koinInject<GameViewModel>()
        val game by gameVM.gameUiModelFlow.collectAsState()

        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            SetSnackbar(
                snackbarDataFlow = gameVM.snackbar,
                modifier = Modifier.align(Alignment.BottomCenter),
            )

            Game(
                game = game,
                onAction = gameVM::onAction,
            )
        }
    }
}
