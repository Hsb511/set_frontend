package com.team23.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team23.ui.button.ActionButton
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun LoginScreen() {
    val loginViewModel = koinInject<LoginViewModel>()

    LoginScreen(
        onAction = loginViewModel::onAction,
    )
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
            text = "Sign in",
            onClick = { onAction(LoginAction.SignIn)}
        )

        Spacer(modifier = Modifier.height(LocalSpacings.current.largeIncreased))

        ActionButton(
            text = "Sign up",
            onClick = { onAction(LoginAction.SignUp)}
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
