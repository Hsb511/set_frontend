package com.team23.ui.gametype

import androidx.compose.foundation.layout.Arrangement
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
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun GameTypeScreen(
    navController: NavHostController = rememberNavController(),
) {
    val gameTypeViewModel = koinInject<GameTypeViewModel>()
    gameTypeViewModel.setNavController(navController)

    GameTypeScreen(
        onAction = gameTypeViewModel::onAction,
    )
}

@Composable
private fun GameTypeScreen(
    onAction: (GameTypeAction) -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        ActionButton(
            text = "Start solo game",
            onClick = { onAction(GameTypeAction.StartSolo)}
        )

        Spacer(modifier = Modifier.height(LocalSpacings.current.largeIncreased))

        ActionButton(
            text = "Start multi game",
            enabled = false,
            onClick = { onAction(GameTypeAction.StartSolo)}
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
