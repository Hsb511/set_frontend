package com.team23.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team23.ui.button.ActionButton
import com.team23.ui.snackbar.SetSnackbar
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun GameTypeScreen(
    navController: NavHostController = rememberNavController(),
) {
    val gameViewModel = koinInject<GameViewModel>()
    gameViewModel.setNavController(navController)

    Box {
        GameTypeScreen(
            onAction = gameViewModel::onAction,
        )

        SetSnackbar(
            snackbarDataFlow = gameViewModel.snackbar,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun GameTypeScreen(
    onAction: (GameAction) -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        ActionButton(
            text = "Start solo game",
            onClick = { onAction(GameAction.StartSolo) }
        )

        Spacer(modifier = Modifier.height(LocalSpacings.current.largeIncreased))

        ActionButton(
            text = "Start multi game",
            enabled = false,
            onClick = { onAction(GameAction.StartMulti) }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameTypeScreenPreview() {
    SetTheme {
        GameTypeScreen()
    }
}
