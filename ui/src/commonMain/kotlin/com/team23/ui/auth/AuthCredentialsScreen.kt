package com.team23.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import com.team23.ui.auth.AuthAction.Auth
import com.team23.ui.button.ActionButton
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun AuthCredentialsScreen(
    authType: AuthType,
) {
    val viewModel = koinInject<AuthViewModel>().apply {
        start()
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stop()
        }
    }

    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .fillMaxSize()
    ) {
        AuthCredentialsScreen(
            authType = authType,
            onAction = viewModel::onAction,
        )
    }
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
            verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        ) {
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var firstname by remember { mutableStateOf("") }
            var lastname by remember { mutableStateOf("") }

            val isUsernameError = username.isBlank()
            val isPasswordError = password.isBlank()

            Text(
                text = "${authType.capitalizedLabel} with your credentials",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                isError = isUsernameError,
                label = {
                    MandatoryText(label = "Username")
                },
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                isError = isPasswordError,
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    MandatoryText(label = "Password")
                },
            )

            if (authType == AuthType.SignUp) {
                TextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = {
                        Text(text = "First name")
                    },
                )
            }

            if (authType == AuthType.SignUp) {
                TextField(
                    value = lastname,
                    onValueChange = { lastname = it },
                    label = {
                        Text(text = "Last name")
                    },
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacings.current.large))

            ActionButton(
                text = authType.capitalizedLabel,
                onClick = {
                    onAction(
                        Auth(
                            type = authType,
                            username = username,
                            password = password,
                            firstname = firstname,
                            lastname = lastname,
                        )
                    )
                },
                enabled = !isUsernameError && !isPasswordError,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun MandatoryText(label: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                append("* ")
            }
            append(label)
        }
    )
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

private class AuthCredentialsPreviewParameter : PreviewParameterProvider<AuthType> {
    override val values: Sequence<AuthType> = AuthType.entries.asSequence()
}
