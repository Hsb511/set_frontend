package com.team23.ui.lobby

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface LobbyUiModel {
    data object Loading : LobbyUiModel

    data class Data(
        val hasAnOngoingSoloGame: Boolean,
        val multiGames: List<MultiGame> = emptyList(),
    ) : LobbyUiModel {

        val hasMultiGames: Boolean
            get() = multiGames.isNotEmpty()

        data class MultiGame(
            val gameId: Uuid,
            val hostName: String,
            val playersCount: Int,
        )
    }
}
