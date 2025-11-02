package com.team23.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.team23.ui.button.ActionButton
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun LoginCredentialsScreen(
    navController: NavController = rememberNavController(),
) {
    val loginViewModel = koinInject<LoginViewModel>()
    loginViewModel.setNavController(navController)

    LoginCredentialsScreen(
        onAction = loginViewModel::onAction,
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun LoginCredentialsScreen(
    onAction: (LoginAction) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.medium),
        ) {
            var userId by remember { mutableStateOf("") }
            val isError = runCatching { Uuid.parse(userId) }.isFailure

            Text(
                text = "Login with your user id",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            TextField(
                value = userId,
                onValueChange = { newValue -> userId = newValue },
                isError = isError,
                supportingText = if (isError) {
                    {
                        Text(text = "Expected either a 36-char string in the standard hex-and-dash UUID format or a 32-char hexadecimal string")
                    }
                } else null,
                label = {
                    Text(text = "userId")
                }
            )

            Spacer(modifier = Modifier.height(LocalSpacings.current.large))

            ActionButton(
                text = "Sign in",
                onClick = {
                    onAction(LoginAction.SignIn(userId))
                },
                enabled = !isError,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
@Preview
private fun LoginCredentialsScreenPreview() {
    SetTheme {
        LoginCredentialsScreen()
    }
}