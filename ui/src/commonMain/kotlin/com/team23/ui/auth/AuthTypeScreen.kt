package com.team23.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team23.ui.button.ActionButton
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AuthTypeScreen(
    navController: NavHostController = rememberNavController(),
) {
    AuthTypeScreen(
        navController = navController,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .fillMaxSize()
    )
}

@Composable
private fun AuthTypeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = LocalSpacings.current.large),
    ) {
        ActionButton(
            text = "Sign up",
            onClick = { navController.navigate(NavigationScreen.SignUpWithCredentials.name )},
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(LocalSpacings.current.largeIncreased))

        ActionButton(
            text = "Sign in",
            onClick = { navController.navigate(NavigationScreen.SignInWithCredentials.name) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(LocalSpacings.current.largeIncreased))

        ActionButton(
            text = "\uD83D\uDEA7 Play as guest \uD83D\uDEA7",
            onClick = { TODO() },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun LoginScreenPreview() {
    SetTheme {
        AuthTypeScreen()
    }
}
