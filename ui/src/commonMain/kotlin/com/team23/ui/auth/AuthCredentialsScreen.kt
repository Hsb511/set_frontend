package com.team23.ui.auth

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
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun AuthCredentialsScreen(
    authType: AuthType,
    navController: NavController = rememberNavController(),
) {
    val authViewModel = koinInject<AuthViewModel>()
    authViewModel.setNavController(navController)

    AuthCredentialsScreen(
        authType = authType,
        onAction = authViewModel::onAction,
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun AuthCredentialsScreen(
    authType: AuthType,
    onAction: (AuthAction) -> Unit,
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
                    onAction(AuthAction.SignIn(userId))
                },
                enabled = !isError,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AuthCredentialsScreenPreview(
    @PreviewParameter(AuthCredentialsPreviewParameter::class) authType: AuthType,
) {
    SetTheme {
        AuthCredentialsScreen(authType) { }
    }
}

private class AuthCredentialsPreviewParameter: PreviewParameterProvider<AuthType> {
    override val values: Sequence<AuthType> = AuthType.entries.asSequence()
}
