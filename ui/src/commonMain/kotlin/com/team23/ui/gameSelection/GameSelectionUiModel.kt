package com.team23.ui.gameSelection

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface GameSelectionUiModel {
    data object Loading : GameSelectionUiModel

    data class Data(
        val hasAnOngoingSoloGame: Boolean,
        val multiGames: List<MultiGame> = emptyList(),
    ) : GameSelectionUiModel {

        val hasMultiGames: Boolean
            get() = multiGames.isNotEmpty()

        data class MultiGame(
            val gameId: Uuid,
            val hostName: String,
            val playersCount: Int,
        )
    }
}
