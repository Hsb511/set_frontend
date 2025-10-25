package com.team23.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.team23.domain.statemachine.GameStateMachine
import com.team23.ui.card.CardUiMapper
import com.team23.ui.game.GameViewModel
import com.team23.ui.game.Game
import com.team23.ui.game.GameUiMapper
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SetTheme {
        val gameSM by remember { mutableStateOf(GameStateMachine()) }
        val cardUiMapper by remember { mutableStateOf(CardUiMapper()) }
        val gameUiMapper by remember { mutableStateOf( GameUiMapper(cardUiMapper)) }
        val gameVM by remember { mutableStateOf( GameViewModel(gameSM, gameUiMapper, cardUiMapper)) }
        val game by gameVM.gameUiModelFlow.collectAsState()

        Game(
            game = game,
            onAction = gameVM::onAction,
        )
    }
}
