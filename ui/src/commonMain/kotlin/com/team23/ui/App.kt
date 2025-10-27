package com.team23.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team23.domain.statemachine.GameStateMachine
import com.team23.domain.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.usecase.IsSetUseCase
import com.team23.ui.card.CardUiMapper
import com.team23.ui.game.Game
import com.team23.ui.game.GameUiMapper
import com.team23.ui.game.GameViewModel
import com.team23.ui.snackbar.SetSnackbar
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SetTheme {
        val coroutineScope = rememberCoroutineScope()
        val isSetUseCase by remember { mutableStateOf(IsSetUseCase()) }
        val gameSM by remember {
            mutableStateOf(GameStateMachine(isSetUseCase, ContainsAtLeastOneSetUseCase(isSetUseCase), coroutineScope))
        }
        val cardUiMapper by remember { mutableStateOf(CardUiMapper()) }
        val gameUiMapper by remember { mutableStateOf(GameUiMapper(cardUiMapper)) }
        val gameVM by remember { mutableStateOf(GameViewModel(gameSM, gameUiMapper, cardUiMapper)) }
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
