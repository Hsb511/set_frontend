package com.team23.ui.navigation

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.team23.domain.game.model.Card
import com.team23.ui.card.Slot
import com.team23.ui.gameSelection.MultiGameMode
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
sealed class NavigationScreen(val name: String) {

    @Serializable
    data object Splash: NavigationScreen(SPLASH_SCREEN_NAME)

    @Serializable
    data object AuthType: NavigationScreen(AUTH_TYPE_SCREEN_NAME)

    @Serializable
    data class AuthCredentials(val authType: AuthType): NavigationScreen(AUTH_CREDENTIALS_SCREEN_NAME) {

        @Serializable
        enum class AuthType(val label: String) {
            SignUp("sign up"), SignIn("sign in");

            val capitalizedLabel
                get() = label.capitalize(Locale.current)
        }
    }

    @Serializable
    data object GameSelection: NavigationScreen(GAME_SELECTION_SCREEN_NAME)

    @Serializable
    data class GameLobby(val gameName: String?, val rawMultiGameMode: String?): NavigationScreen(GAME_LOBBY_SCREEN_NAME)

    @Serializable
    data class Game(val type: Type): NavigationScreen(GAME_SCREEN_NAME) {

        @Serializable
        sealed interface Type {

            @Serializable
            data class Solo(val forceCreate: Boolean): Type

            @Serializable
            data class Multi(
                val gameId: Uuid,
                val deck: List<Slot.CardUiModel>,
                val table: List<Slot.CardUiModel>,
                val mode: MultiGameMode,
            ): Type
        }
    }

    @Serializable
    data object Settings: NavigationScreen("Settings")

    companion object {
        private const val SPLASH_SCREEN_NAME = "Splash"
        private const val AUTH_TYPE_SCREEN_NAME = "AuthType"
        private const val AUTH_CREDENTIALS_SCREEN_NAME = "AuthCredentials"
        private const val GAME_SELECTION_SCREEN_NAME = "GameSelection"
        private const val GAME_LOBBY_SCREEN_NAME = "GameLobby"
        private const val GAME_SCREEN_NAME = "Game"
    }
}
