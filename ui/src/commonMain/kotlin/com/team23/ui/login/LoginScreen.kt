package com.team23.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
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
fun LoginScreen(
    navController: NavHostController = rememberNavController(),
) {
    val loginViewModel = koinInject<LoginViewModel>()
    loginViewModel.setNavController(navController)

    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .fillMaxSize()
    ) {
        LoginScreen(
            onAction = loginViewModel::onAction,
        )

        SetSnackbar(
            snackbarDataFlow = loginViewModel.snackbar,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun LoginScreen(
    onAction: (LoginAction) -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        ActionButton(
            text = "play without an account",
            onClick = { onAction(LoginAction.NavigateToSignUp)}
        )

        Spacer(modifier = Modifier.height(LocalSpacings.current.largeIncreased))

        ActionButton(
            text = "\uD83D\uDEA7 sign in with userId \uD83D\uDEA7",
            onClick = { onAction(LoginAction.NavigateToSignIn)}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun LoginScreenPreview() {
    SetTheme {
        LoginScreen()
    }
}
