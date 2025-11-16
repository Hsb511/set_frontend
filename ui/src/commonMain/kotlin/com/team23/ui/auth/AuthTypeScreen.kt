package com.team23.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team23.ui.button.ActionButton
import com.team23.ui.snackbar.SetSnackbar
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun AuthTypeScreen(
    navController: NavHostController = rememberNavController(),
) {
    val authViewModel = koinInject<AuthViewModel>()
    authViewModel.setNavController(navController)

    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .fillMaxSize()
    ) {
        AuthTypeScreen(
            onAction = authViewModel::onAction,
        )

        SetSnackbar(
            snackbarDataFlow = authViewModel.snackbar,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun AuthTypeScreen(
    onAction: (AuthAction) -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = LocalSpacings.current.large),
    ) {
        ActionButton(
            text = "Sign up",
            onClick = { onAction(AuthAction.NavigateToSignUp)},
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(LocalSpacings.current.largeIncreased))

        ActionButton(
            text = "Sign in",
            onClick = { onAction(AuthAction.NavigateToSignIn)},
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(LocalSpacings.current.largeIncreased))

        ActionButton(
            text = "\uD83D\uDEA7 Play as guest \uD83D\uDEA7",
            onClick = { onAction(AuthAction.NavigateToSignIn)},
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun LoginScreenPreview() {
    SetTheme {
        AuthTypeScreen { }
    }
}
